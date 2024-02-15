package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameSocketController {

	private final GameService gameService;
	private final SimpMessageSendingOperations sendingOperations;

	// 게임 시작 메시지 처리
	@MessageMapping("/game-room/{roomId}/start")
	public void startGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
		gameService.startGame(roomId, memberId);
	}

	// @MessageExceptionHandler(CustomException.class)
	// public void handleCustomException(CustomException e) {
	// 	log.warn(">>>>>> Error occurred in Message Mapping: ", e);
	// 	sendingOperations.convertAndSend("/from/error", e.getMessage());
	// }
}
