package com.pgms.api.domain.game.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.exception.GameException;
import com.pgms.api.socket.dto.Message;
import com.pgms.api.socket.dto.MessageType;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.common.GameErrorCode;
import com.pgms.coredomain.domain.common.GameRoomErrorCode;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameRoomMember;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coredomain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomService {

	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;
	private final SimpMessageSendingOperations sendingOperations;

	// ============================== 게임방 생성 ==============================
	public Long createRoom(Long memberId, GameRoomCreateRequest request) {
		// 유저 있는지 확인
		Member member = getMember(memberId);

		// 유저가 다른 방에 들어가 있는지 확인 -> 있으면 gameRoomMember 삭제
		validateMemberAlreadyEntered(memberId);

		// 방 생성
		GameRoom gameRoom = request.toEntity(memberId);

		// 방에 입장한 유저 정보 생성
		GameRoomMember gameRoomMember = GameRoomMember.builder()
			.gameRoom(gameRoom)
			.memberId(memberId)
			.nickname(member.getNickname())
			.webSessionId(null)
			.readyStatus(true)
			.build();

		log.info(">>>>>>>>>> 접속한 유저의 정보를 토대로 gameRoomMember 생성 - 방장 {}", gameRoomMember);
		log.info(">>>>>>>>>> 방을 생성하는 유저는 방장이므로 항상 준비 상태 {}", gameRoomMember.isReadyStatus());

		// 게임방 DB에 저장 및 입장중인 유저 정보 저장
		gameRoom.enterRoom();
		GameRoom savedGameRoom = gameRoomRepository.save(gameRoom);
		gameRoomMemberRepository.save(gameRoomMember);

		// 게임방 리스트 조회
		sseEmitters.updateGameRoom(sseService.getRooms());
		return savedGameRoom.getId();
	}

	// ============================== 게임방 입장 ==============================
	public Long enterGameRoom(Long memberId, Long roomId) {
		// memberId로 유저가 존재하는지
		Member member = getMember(memberId);

		// roomId로 방이 존재하는지
		GameRoom gameRoom = getGameRoom(roomId);

		// 방 입장이 가능한지 검증
		validateGameRoomEnableEnter(gameRoom);

		// 유저가 이미 방에 들어가 있는지 확인 -> 들어가 있으면 기존 방 삭제
		validateMemberAlreadyEntered(memberId);

		// 새롭게 게임방에 입장하는 유저 생성
		GameRoomMember gameRoomMember = GameRoomMember.builder()
			.gameRoom(gameRoom)
			.memberId(memberId)
			.nickname(member.getNickname())
			.webSessionId(null)
			.readyStatus(false)
			.build();

		gameRoom.enterRoom();
		gameRoomMemberRepository.save(gameRoomMember);

		// 대기실 리스트 업데이트
		sseEmitters.updateGameRoom(sseService.getRooms());

		return roomId;
	}

	// ============================== 입장 후 게임방 멤버 세션 아이디 설정 ==============================
	public void updateSessionId(Long memberId, String sessionId) {

		// 게임 룸에는 이미 입장한 상태
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
		gameRoomMember.updateSessionId(sessionId);

		// 현재 게임방 유저에 대한 정보 보냄
		sendGameRoomInfo(gameRoomMember.getGameRoom(), MessageType.ENTER);
	}

	// ============================== 게임방 퇴장 ==============================
	public void exitGameRoom(String sessionId) {
		// 세션 ID로 게임방 멤버 찾아서 제거
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByWebSessionId(sessionId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
		final GameRoom gameRoom = gameRoomMember.getGameRoom();

		gameRoomMemberRepository.delete(gameRoomMember);
		gameRoom.exitRoom();

		// 현재 인원 0 명이면 방까지 제거
		if (gameRoom.getCurrentPlayer() == 0) {
			gameRoomRepository.delete(gameRoom);
			return;
		}

		// 방장이 나갔으면 다음 방장 지정
		final List<GameRoomMember> leftGameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(gameRoom.getId());

		if (gameRoom.getHostId().equals(gameRoomMember.getMemberId())) {
			final GameRoomMember nextHost = leftGameRoomMembers.get(0);
			nextHost.updateReadyStatus(true);
			gameRoom.updateHostId(nextHost.getMemberId());
		}

		// 구독된 사람들에게 메세지
		sendingOperations.convertAndSend("/from/game-room/" + gameRoom.getId(),
			Message.builder()
				.type(MessageType.EXIT)
				.roomId(gameRoom.getId())
				.allMembers(leftGameRoomMembers.stream().map(GameRoomMemberGetResponse::from).toList())
				.exitMemberId(gameRoomMember.getMemberId())
				.build()
				.toJson()
		);

		sseEmitters.updateGameRoom(sseService.getRooms());
	}

	// ============================== 게임방 멤버 준비 상태 변경 ==============================
	public void updateReadyStatus(Long roomId, Long memberId) {
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
		if (!gameRoomMember.getGameRoom().getHostId().equals(memberId)) {
			gameRoomMember.updateReadyStatus(!gameRoomMember.isReadyStatus());
			sendGameRoomInfo(gameRoomMember.getGameRoom(), MessageType.READY);
		}
	}

	// ============================== 게임방 멤버 강퇴 ==============================
	public void kickGameRoomMember(Long roomId, Long memberId, Long kickedId) {
		final GameRoom gameRoom = getGameRoom(roomId);

		// 시작버튼 누른 유저 검증 (있는 유저인지 & 방장인지)
		if (!gameRoom.getHostId().equals(memberId)) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_HOST_MISMATCH);
		}

		// 강퇴 당하는 유저 존재하는지 검증
		final GameRoomMember kickedMember = gameRoomMemberRepository.findByMemberId(kickedId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));

		// GameRoomMember에서 강퇴 당한 유저 삭제 처리
		gameRoomMemberRepository.delete(kickedMember);

		// GameRoomMember 리스트 가져오기
		List<GameRoomMember> leftGameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);

		// 방장이면 -> 강퇴 처리 (메시지 던지기)
		sendingOperations.convertAndSend("/from/game-room/" + roomId,
			Message.builder()
				.type(MessageType.KICKED)
				.roomId(gameRoom.getId())
				.allMembers(leftGameRoomMembers.stream().map(GameRoomMemberGetResponse::from).toList())
				.exitMemberId(kickedId)
				.build()
				.toJson());

		sseEmitters.updateGameRoom(sseService.getRooms());
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GameException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private void validateGameRoomEnableEnter(GameRoom gameRoom) {
		// 게임 방 시작 상태 확인 -> True 이면 입장 불가능
		if (gameRoom.isPlaying()) {
			throw new GameException(GameErrorCode.GAME_ALREADY_STARTED);
		}

		// 게임 풀방인지 확인 -> 풀방이면 입장 불가능
		if (gameRoom.isFull()) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_FULL);
		}
	}

	private void validateMemberAlreadyEntered(Long memberId) {
		gameRoomMemberRepository.findByMemberId(memberId).ifPresent(gameRoomMember -> {
			gameRoomMember.getGameRoom().exitRoom();
			gameRoomMemberRepository.delete(gameRoomMember);
		});
	}

	private void sendGameRoomInfo(GameRoom gameRoom, MessageType type) {
		Long roomId = gameRoom.getId();
		final List<GameRoomMemberGetResponse> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId)
			.stream()
			.map(GameRoomMemberGetResponse::from)
			.toList();

		sendingOperations.convertAndSend(
			"/from/game-room/" + roomId,
			Message.builder()
				.type(type)
				.roomId(roomId)
				.allMembers(gameRoomMembers)
				.roomInfo(GameRoomGetResponse.from(gameRoom))
				.build()
				.toJson()
		);
	}
}
