package com.pgms.apisocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.pgms.apisocket.dto.GameRoomEnterResponse;
import com.pgms.apisocket.dto.GameRoomExitResponse;
import com.pgms.apisocket.service.GameRoomSocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameRoomSocketController {

	private final GameRoomSocketService gameRoomSocketService;

	// 게임방 입장
	@MessageMapping("/game-room/{roomId}/enter")
	@SendTo("/from/game-room/{roomId}/enter")
	public GameRoomEnterResponse enterGameRoom(
		@Header("MemberId") String memberId,
		@DestinationVariable Long roomId,
		StompHeaderAccessor stompHeaderAccessor) {
		log.info(">>>>>> enterGameRoom : roomId = {}, memberId = {}", roomId, memberId);
		return gameRoomSocketService.enterGameRoom(roomId, Long.parseLong(memberId));
	}

	// 게임방 퇴장
	@MessageMapping("/game-room/{roomId}/exit")
	@SendTo("/from/game-room/{roomId}/exit")
	public GameRoomExitResponse exitGameRoom(
		@Header("MemberId") String memberId,
		@DestinationVariable Long roomId,
		StompHeaderAccessor stompHeaderAccessor) {
		log.info(">>>>>> exitGameRoom : roomId = {}, memberId = {}", roomId, memberId);
		return gameRoomSocketService.exitGameRoom(roomId, Long.parseLong(memberId));
	}
	// 게임 준비

	// 게임 시작

}
