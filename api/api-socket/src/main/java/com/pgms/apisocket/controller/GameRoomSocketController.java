package com.pgms.apisocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.pgms.apisocket.dto.GameRoomEnterResponse;
import com.pgms.apisocket.dto.GameRoomExitResponse;
import com.pgms.apisocket.service.GameRoomSocketService;
import com.pgms.coresecurity.resolver.CurrentAccount;

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
		@CurrentAccount Long memberId,
		@DestinationVariable Long roomId) {
		return gameRoomSocketService.enterGameRoom(roomId, memberId);
	}

	// 게임방 퇴장
	@MessageMapping("/game-room/{roomId}/exit")
	@SendTo("/from/game-room/{roomId}/exit")
	public GameRoomExitResponse exitGameRoom(
		@CurrentAccount Long memberId,
		@DestinationVariable Long roomId) {
		return gameRoomSocketService.exitGameRoom(roomId, memberId);
	}
	// 게임 준비

	// 게임 시작

}
