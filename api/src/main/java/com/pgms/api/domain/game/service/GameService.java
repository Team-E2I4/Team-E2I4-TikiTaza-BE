package com.pgms.api.domain.game.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.exception.GameException;
import com.pgms.api.socket.dto.GameInfoUpdateRequest;
import com.pgms.api.socket.dto.Message;
import com.pgms.api.socket.dto.MessageType;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.common.GameRoomErrorCode;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameRoomMember;
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

	// ============================== 게임 중 실시간 업데이트 통신 ==============================
	public void updateGameInfoInRealTime(Long memberId, Long roomId, GameInfoUpdateRequest gameInfoUpdateRequest) {
	}

	// ============================== 게임 종료 ==============================
	public void finishGame(Long memberId, Long roomId) {
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private GameRoomMember getGameRoomMember(Long memberId) {
		return gameRoomMemberRepository.findById(memberId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));
	}
}
