package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameSocketController {

	private final GameService gameService;

	// 게임 시작 메시지 처리
	@MessageMapping("/game-room/{roomId}/start")
	public void startGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
		gameService.startGame(roomId, memberId);
	}
}
