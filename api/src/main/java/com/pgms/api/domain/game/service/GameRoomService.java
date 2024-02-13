package com.pgms.api.domain.game.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.exception.GameException;
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
			.readyStatus(true)
			.build();

		gameRoom.enterRoom();
		gameRoomMemberRepository.save(gameRoomMember);
		sseEmitters.updateGameRoom(sseService.getRooms());
		return roomId;
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GameException(MemberErrorCode.MEMBER_NOT_FOUND));
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
		if (gameRoomMemberRepository.existsByMemberId(memberId)) {
			gameRoomMemberRepository.deleteByMemberId(memberId);
		}
	}
}
