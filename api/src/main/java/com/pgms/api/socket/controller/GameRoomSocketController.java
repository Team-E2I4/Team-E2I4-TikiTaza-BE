package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class
GameRoomSocketController {

	private final GameRoomService gameRoomService;

	// 게임방 입장 시 세션 아이디 설정
	@MessageMapping("/game-room/{roomId}/enter")
	public void setSessionId(
		@Header("AccountId") Long accountId,
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor headerAccessor) {
		final String sessionId = headerAccessor.getSessionId();
		log.info(">>>>>> enterGameRoom : roomId = {}, accountId = {}, sessionId = {}", roomId, accountId, sessionId);
		gameRoomService.updateSessionId(roomId, accountId, sessionId);
	}

	// 게임 준비 처리
	@MessageMapping("/game-room/{roomId}/ready")
	public void updateReadyStatus(
		@Header("AccountId") Long accountId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Member Ready : roomId = {}, accountId = {}", roomId, accountId);
		gameRoomService.updateReadyStatus(accountId);
	}

	// 게임방 강퇴 처리
	@MessageMapping("/game-room/{roomId}/kick/{kickedId}")
	public void kickGameRoomMember(
		@Header("AccountId") Long accountId,
		@DestinationVariable Long roomId,
		@DestinationVariable Long kickedId) {
		log.info(">>>>>> Member Kicked : roomId = {}, accountId = {}, kickedId = {}", roomId, accountId, kickedId);
		gameRoomService.kickGameRoomMember(roomId, accountId, kickedId);
	}

	// 게임방 게임 시작 처리
	@MessageMapping("/game-room/{roomId}/start")
	public void startGame(
		@Header("AccountId") Long accountId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Game Start : roomId = {}, accountId = {}", roomId, accountId);
		gameRoomService.startGame(roomId, accountId);
	}
}
