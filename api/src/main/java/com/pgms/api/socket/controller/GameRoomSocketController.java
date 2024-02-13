package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameRoomSocketController {

	private final GameRoomService gameRoomService;

	// 세션 ID를 게임 룸 멤버에 추가
	@MessageMapping("/game-room/{roomId}/enter")
	@SendTo("/from/game-room/{roomId}/enter")
	public void setSessionId(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor headerAccessor) {
		final String sessionId = headerAccessor.getSessionId();
		log.info(">>>>>> enterGameRoom : roomId = {}, memberId = {}, sessionId = {}", roomId, memberId, sessionId);
		gameRoomService.enterSessionId(roomId, memberId, sessionId);
	}
	//
	// // 게임방 퇴장
	// @MessageMapping("/game-room/{roomId}/exit")
	// @SendTo("/from/game-room/{roomId}/exit")
	// public GameRoomExitResponse exitGameRoom(
	// 	@Header("MemberId") Long memberId,
	// 	@DestinationVariable Long roomId) {
	// 	log.info(">>>>>> exitGameRoom : roomId = {}, memberId = {}", roomId, memberId);
	// 	return gameRoomSocketService.exitGameRoom(roomId, memberId);
	// }
	//
	// // 게임 시작 메시지 처리
	// @MessageMapping("/game-room/{roomId}/start")
	// @SendTo("/from/game-room/{roomId}/enter")
	// public GameStartResponse startGame(@DestinationVariable Long roomId) {
	// 	// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
	// 	log.info(">>>>>> Game Start !!!");
	// 	return gameRoomSocketService.startGame(roomId);
	// }
}
