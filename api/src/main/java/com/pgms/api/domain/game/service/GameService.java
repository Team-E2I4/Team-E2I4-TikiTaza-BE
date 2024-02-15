package com.pgms.api.domain.game.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.exception.GameException;
import com.pgms.api.socket.dto.GameInfoUpdateRequest;
import com.pgms.api.socket.dto.GameQuestionGetResponse;
import com.pgms.api.socket.dto.Message;
import com.pgms.api.socket.dto.MessageType;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.common.GameRoomErrorCode;
import com.pgms.coredomain.domain.game.GameInfo;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coredomain.repository.GameInfoRepository;
import com.pgms.coredomain.repository.GameQuestionRepository;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final GameQuestionRepository gameQuestionRepository;
	private final GameInfoRepository gameInfoRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;
	private final SimpMessageSendingOperations sendingOperations;

	// ============================== 게임 시작 ==============================
	public void startGame(Long roomId, Long memberId) {
		// 현재 방 정보 가져오기
		final GameRoom gameRoom = getGameRoom(roomId);

		// 시작버튼 누른 유저 검증 (있는 유저인지 & 방장인지)
		if (!gameRoom.getHostId().equals(memberId)) {
			throw new GameException(GameRoomErrorCode.GAME_ROOM_HOST_MISMATCH);
		}

		// 방에 있는 인원들 준비상태 확인
		if (gameRoom.isAllReady()) {
			// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
			gameRoom.updateGameRoomStatus(true);

			// 게임 시작 메세지 뿌리기
			sendingOperations.convertAndSend(
				"/from/game-room/" + gameRoom.getId(),
				Message.builder()
					.type(MessageType.START)
					.build()
					.toJson()
			);

			// 방 정보 뿌리기 -> 게임 중인 방은 로비에서 보이면 안되니까
			sseEmitters.updateGameRoom(sseService.getRooms());
		} else {
			// 실패 메세지 뿌리기
			sendingOperations.convertAndSend(
				"/from/game-room/" + gameRoom.getId(),
				Message.builder()
					.type(MessageType.START_DENIED)
					.build()
					.toJson()
			);
		}
	}

	// ============================== 모두 입장한걸 확인하고 문제와 유저 정보 전송 ==============================
	public void roundStart(Long roomId, Long memberId) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);

		// 카운트만 올려줌
		gameInfo.enter();

		if (gameInfo.isAllEntered(gameRoom.getCurrentPlayer())) {
			// 문제와 유저 정보 전송
			final GameType gameType = gameRoom.getGameType();
			final List<GameQuestionGetResponse> questions = gameQuestionRepository.findByGameTypeAndCount(gameType,
					PageRequest.of(0, gameType.getQuestionCount()))
				.stream()
				.map(GameQuestionGetResponse::of)
				.toList();

			final List<GameRoomMemberGetResponse> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId)
				.stream()
				.map(GameRoomMemberGetResponse::from)
				.toList();

			sendingOperations.convertAndSend(
				"/from/game/" + gameRoom.getId() + "/round-start",
				Message.builder()
					.type(MessageType.ROUND_START)
					.allMembers(gameRoomMembers)
					.questions(questions)
					.build()
					.toJson());
		}

		// 입장인원 == GameRoom.getMemberCount 진짜시작 / 제출인원 == GameRoom.getMemberCount 진짜 다음라운드
		// 전부 입장했다 -> 문제와 유저 정보 전송 (진짜 시작) /from/game/{roomId}/round-start
	}

	// ============================== 게임 중 실시간 업데이트 통신 ==============================
	public void updateGameInfoInRealTime(Long memberId, Long roomId, GameInfoUpdateRequest gameInfoUpdateRequest) {

	}

	// ============================== 게임 종료 & 다음 라운드 시작 ==============================
	public void finishGame(Long memberId, Long roomId) {
		// 게임방에 있는 모두가 게임 종료를 누르면 게임 종료 처리
		// 마지막 라운드면 /from/game/{roomId}/round-finish 로 전송
		// 마지막 라운드가 아니면 /from/game/{roomId}/round-start 로 문제 전송
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private GameInfo getGameInfo(Long roomId) {
		return gameInfoRepository.findByGameRoomId(roomId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_INFO_NOT_FOUND));
	}
}
