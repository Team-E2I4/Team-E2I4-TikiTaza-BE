package com.pgms.api.domain.game.service;

import static com.pgms.api.socket.dto.response.GameRoomMessageType.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.dto.request.GameRoomEnterRequest;
import com.pgms.api.domain.game.dto.request.GameRoomUpdateRequest;
import com.pgms.api.domain.game.dto.response.GameRoomCreateResponse;
import com.pgms.api.domain.game.dto.response.GameRoomEnterResponse;
import com.pgms.api.domain.game.dto.response.GameRoomInviteCodeResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.global.exception.GameException;
import com.pgms.api.global.exception.SocketException;
import com.pgms.api.socket.dto.response.GameRoomMessageType;
import com.pgms.api.socket.service.GameRoomMessageService;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.game.GameInfo;
import com.pgms.coredomain.domain.game.GameRank;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameRoomMember;
import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coredomain.exception.GameErrorCode;
import com.pgms.coredomain.exception.GameRoomErrorCode;
import com.pgms.coredomain.exception.MemberErrorCode;
import com.pgms.coredomain.repository.GameInfoRepository;
import com.pgms.coredomain.repository.GameRankRepository;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.repository.GuestRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;
import com.pgms.coresecurity.resolver.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomService {

	private final GameRoomMessageService gameRoomMessageService;
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final GameInfoRepository gameInfoRepository;
	private final RedisRepository redisRepository;
	private final GuestRepository guestRepository;
	private final GameRankRepository gameRankRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;

	// ============================== 게임방 생성 ==============================
	public GameRoomCreateResponse createGameRoom(Account account, GameRoomCreateRequest request) {
		// 유저/게스트가 존재하는지 확인
		validateExistMember(account);

		// 유저가 다른 방에 들어가 있는지 확인
		validateMemberAlreadyEntered(account.id(), null);

		// 랜덤 초대코드 생성
		String inviteCode = createInviteCode();

		// 방 생성
		GameRoom gameRoom = request.toEntity(account.id(), inviteCode);

		// 방에 입장한 유저 정보 생성
		GameRoomMember gameRoomMember = GameRoomMember.builder()
			.gameRoom(gameRoom)
			.memberId(account.id())
			.nickname(account.nickname())
			.readyStatus(true)
			.build();

		// 게임방 DB에 저장 및 입장중인 유저 정보 저장
		gameRoom.enterRoom();
		GameRoom savedGameRoom = gameRoomRepository.save(gameRoom);
		gameRoomMemberRepository.save(gameRoomMember);

		// Redis -> 초대 코드 : 방 번호 저장
		redisRepository.saveInviteCode(inviteCode, savedGameRoom.getId());

		// 게임방 리스트 조회
		sseEmitters.updateGameRoom(sseService.getRooms());
		return GameRoomCreateResponse.from(savedGameRoom.getId());
	}

	// ============================== 게임방 설정 변경 ==============================
	public void updateRoom(Account account, Long roomId, GameRoomUpdateRequest request) {
		validateExistMember(account);
		GameRoom gameRoom = getGameRoom(roomId);

		validateGameRoomHost(roomId, account.id(), gameRoom);

		validateGameRoomUpdate(gameRoom, request);

		gameRoom.updateGameRoom(
			request.title(),
			request.password(),
			request.maxPlayer(),
			request.round(),
			GameType.of(request.gameType()));

		// 모든 유저를 준비 해제
		gameRoom.getGameRoomMembers().forEach(gameRoomMember -> {
			if (!gameRoom.isHost(gameRoomMember.getMemberId())) {
				gameRoomMember.updateReadyStatus(false);
			}
		});

		// 구독된 사람들에게 메세지
		sendGameRoomInfo(gameRoom, MODIFIED);
		sseEmitters.updateGameRoom(sseService.getRooms());
	}

	// ============================== 게임방 입장 ==============================
	public GameRoomEnterResponse enterGameRoom(Account account, Long roomId, GameRoomEnterRequest request) {
		// 유저가 존재하는지
		validateExistMember(account);

		// roomId로 방이 존재하는지
		GameRoom gameRoom = getGameRoom(roomId);

		// 방 패스워드 검증
		validateGameRoomPassword(gameRoom, request);

		// 방 입장이 가능한지 검증
		validateGameRoomEntrance(gameRoom);

		// 유저가 이미 방에 들어가 있는지 확인 -> 들어가 있으면 기존 방 삭제
		validateMemberAlreadyEntered(account.id(), roomId);

		// 새롭게 게임방에 입장하는 유저 생성
		GameRoomMember gameRoomMember = GameRoomMember.builder()
			.gameRoom(gameRoom)
			.memberId(account.id())
			.nickname(account.nickname())
			.readyStatus(false)
			.build();

		gameRoom.enterRoom();
		gameRoomMemberRepository.save(gameRoomMember);

		// 대기실 리스트 업데이트
		sseEmitters.updateGameRoom(sseService.getRooms());
		return GameRoomEnterResponse.from(roomId, account.id());
	}

	// ============================== 입장 후 게임방 멤버 세션 아이디 설정 ==============================
	public void updateSessionId(Long roomId, Long memberId, String sessionId) {
		// 게임 룸에는 이미 입장한 상태
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
		gameRoomMember.updateSessionId(sessionId);

		// 현재 게임방 유저에 대한 정보 보냄
		sendGameRoomInfo(gameRoomMember.getGameRoom(), ENTER);
	}

	// ============================== 게임방 퇴장 ==============================
	public void exitGameRoom(String sessionId) {
		// 세션 ID로 게임방 멤버 찾아서 제거
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByWebSessionId(sessionId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
		final GameRoom gameRoom = gameRoomMember.getGameRoom();

		gameRoomMemberRepository.delete(gameRoomMember);
		gameRoom.exitRoom();

		// 현재 인원 0 명이면 방 & Redis 초대 코드 제거
		if (gameRoom.isEmpty()) {
			cleanUpGameRoom(gameRoom);
			sseEmitters.updateGameRoom(sseService.getRooms());
			return;
		}

		// 방장이 나가면 다음 방장 지정
		final List<GameRoomMember> leftGameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(gameRoom.getId());

		if (gameRoom.isHost(gameRoomMember.getMemberId())) {
			final GameRoomMember nextHost = leftGameRoomMembers.get(0);
			nextHost.updateReadyStatus(true);
			gameRoom.updateHostId(nextHost.getMemberId());
		}

		final List<GameRoomMemberGetResponse> leftGameRoomMemberResponses = getGameRoomMembersWithRankings(
			leftGameRoomMembers);

		// 구독된 사람들에게 메세지
		gameRoomMessageService.sendExitGameRoomMessage(gameRoom, gameRoomMember.getId(), leftGameRoomMemberResponses);
		sseEmitters.updateGameRoom(sseService.getRooms());
	}

	// ============================== 게임방 멤버 준비 상태 변경 ==============================
	public void updateReadyStatus(Long accountId) {
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByMemberId(accountId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));

		if (!gameRoomMember.getGameRoom().getHostId().equals(accountId)) {
			gameRoomMember.updateReadyStatus(!gameRoomMember.isReadyStatus());
			sendGameRoomInfo(gameRoomMember.getGameRoom(), READY);
		}
	}

	// ============================== 게임방 멤버 강퇴 ==============================
	public void kickGameRoomMember(Long roomId, Long accountId, Long kickedId) {
		final GameRoom gameRoom = getGameRoom(roomId);

		// 시작버튼 누른 유저 검증 (있는 유저인지 & 방장인지)
		validateGameRoomHost(roomId, accountId, gameRoom);

		// 강퇴 당하는 유저 존재하는지 검증
		gameRoomMemberRepository.findByMemberId(kickedId)
			.orElseThrow(() -> new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));

		// 방장이면 -> 강퇴 처리 (메시지 던지기) -> KICKED : roomId & exitMemberId
		gameRoomMessageService.sendKickMessage(roomId, kickedId);
	}

	public GameRoomInviteCodeResponse getRoomIdByInviteCode(Account account, String inviteCode) {
		validateExistMember(account);
		if (!redisRepository.hasKey(inviteCode)) {
			throw new GameException(GameRoomErrorCode.INVALID_INVITE_CODE);
		}
		return GameRoomInviteCodeResponse.from(Long.valueOf(redisRepository.get(inviteCode).toString()));
	}

	// ============================== 게임 시작 ==============================
	public void startGame(Long roomId, Long accountId) {
		// 현재 방 정보 가져오기
		final GameRoom gameRoom = getGameRoom(roomId);

		// 시작버튼 누른 유저 검증 (있는 유저인지 & 방장인지)
		if (!gameRoom.getHostId().equals(accountId)) {
			throw new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_HOST_MISMATCH);
		}

		// 방에 있는 인원들 준비상태 확인
		if (gameRoom.isAllReady()) {
			// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
			gameRoom.updateGameRoomStatus(true);
			gameInfoRepository.save(new GameInfo(roomId));

			// 게임 시작 메세지 뿌리기
			gameRoomMessageService.sendGameStartOrFailMessage(START, roomId);
			// 방 정보 뿌리기 -> 게임 중인 방은 로비에서 보이면 안되니까
			sseEmitters.updateGameRoom(sseService.getRooms());
		} else {
			// 실패 메세지 뿌리기
			gameRoomMessageService.sendGameStartOrFailMessage(START_DENIED, roomId);
		}
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private String createInviteCode() {
		String inviteCode;
		do {
			UUID uuid = UUID.randomUUID();
			String uuidAsString = uuid.toString().replace("-", "");
			inviteCode = uuidAsString.substring(0, 8);
		} while (redisRepository.hasKey(inviteCode));
		return inviteCode;
	}

	private void validateExistMember(Account account) {
		if (account.isGuest()) {
			guestRepository.findById(account.id())
				.orElseThrow(() -> new GameException(MemberErrorCode.MEMBER_NOT_FOUND));
		} else {
			memberRepository.findById(account.id())
				.orElseThrow(() -> new GameException(MemberErrorCode.MEMBER_NOT_FOUND));
		}
	}

	private void validateGameRoomHost(Long roomId, Long memberId, GameRoom gameRoom) {
		if (!gameRoom.isHost(memberId)) {
			throw new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_HOST_MISMATCH);
		}
	}

	private void validateGameRoomPassword(GameRoom gameRoom, GameRoomEnterRequest request) {
		if (gameRoom.isPrivate() && !(gameRoom.getPassword().equals(request.password()))) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_PASSWORD_MISMATCH);
		}
	}

	private void validateGameRoomEntrance(GameRoom gameRoom) {
		// 게임 방 시작 상태 확인 -> True 이면 입장 불가능
		if (gameRoom.isPlaying()) {
			throw new GameException(GameErrorCode.GAME_ALREADY_STARTED);
		}

		// 게임 풀방인지 확인 -> 풀방이면 입장 불가능
		if (gameRoom.isFull()) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_FULL);
		}
	}

	private void validateGameRoomUpdate(GameRoom gameRoom, GameRoomUpdateRequest request) {
		if (gameRoom.isPlaying()) {
			throw new GameException(GameErrorCode.GAME_ALREADY_STARTED);
		}

		if (request.maxPlayer() < gameRoom.getCurrentPlayer()) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_MAX_PLAYER_MISMATCH);
		}
	}

	private void validateMemberAlreadyEntered(Long memberId, Long newRoomId) {
		gameRoomMemberRepository.findByMemberId(memberId).ifPresent(gameRoomMember -> {
			final GameRoom gameRoom = gameRoomMember.getGameRoom();
			if (gameRoom.getId().equals(newRoomId)) {
				throw new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_IN_SAME_ROOM);
			}
			gameRoom.exitRoom();
			if (gameRoom.isEmpty()) {
				cleanUpGameRoom(gameRoom);
			}
			gameRoomMemberRepository.delete(gameRoomMember);
		});
	}

	private void cleanUpGameRoom(GameRoom gameRoom) {
		gameRoomRepository.delete(gameRoom);
		deleteInviteCodeFromRedis(gameRoom.getInviteCode());
	}

	private void deleteInviteCodeFromRedis(String inviteCode) {
		if (redisRepository.hasKey(inviteCode)) {
			redisRepository.delete(inviteCode);
		}
	}

	private void sendGameRoomInfo(GameRoom gameRoom, GameRoomMessageType type) {
		List<GameRoomMemberGetResponse> gameRoomMembers = getGameRoomMembersWithRankings(gameRoom.getGameRoomMembers());
		gameRoomMessageService.sendGameRoomInfoMessage(type, gameRoom, gameRoomMembers);
	}

	private List<GameRoomMemberGetResponse> getGameRoomMembersWithRankings(List<GameRoomMember> gameRoomMembers) {
		return gameRoomMembers.stream()
			.map(gameRoomMember -> {
				Optional<GameRank> ranks = gameRankRepository.findTotalRanking(gameRoomMember.getMemberId());
				int ranking = ranks.map(GameRank::getRanking).orElse(-1);
				return GameRoomMemberGetResponse.from(gameRoomMember, ranking);
			})
			.toList();
	}
}
