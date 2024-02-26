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
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor headerAccessor) {
		final String sessionId = headerAccessor.getSessionId();
		log.info(">>>>>> enterGameRoom : roomId = {}, memberId = {}, sessionId = {}", roomId, memberId, sessionId);
		gameRoomService.updateSessionId(roomId, memberId, sessionId);
	}

	// 게임 준비 처리
	@MessageMapping("/game-room/{roomId}/ready")
	public void updateReadyStatus(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Member Ready : roomId = {}, memberId = {}", roomId, memberId);
		gameRoomService.updateReadyStatus(memberId);
	}

	// 게임방 강퇴 처리
	@MessageMapping("/game-room/{roomId}/kick/{kickedId}")
	public void kickGameRoomMember(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@DestinationVariable Long kickedId) {
		log.info(">>>>>> Member Kicked : roomId = {}, memberId = {}, kickedId = {}", roomId, memberId, kickedId);
		gameRoomService.kickGameRoomMember(roomId, memberId, kickedId);
	}

	// 게임방 게임 시작 처리
	@MessageMapping("/game-room/{roomId}/start")
	public void startGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Game Start : roomId = {}, memberId = {}", roomId, memberId);
		gameRoomService.startGame(roomId, memberId);
	}
}
