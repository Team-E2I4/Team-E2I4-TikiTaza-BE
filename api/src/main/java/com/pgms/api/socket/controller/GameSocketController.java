package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameService;
import com.pgms.api.socket.dto.GameInfoUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameSocketController {

	private final GameService gameService;

	// 게임 시작 메시지 처리
	@MessageMapping("/game/{roomId}/start")
	public void startGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Game Start : roomId = {}, memberId = {}", roomId, memberId);
		gameService.startGame(roomId, memberId);
	}

	// 게임 중 실시간 정보 업데이트
	@MessageMapping("/game/{roomId}/update")
	public void updateGameInfoInRealTime(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload GameInfoUpdateRequest gameInfoUpdateRequest) {
		gameService.updateGameInfoInRealTime(memberId, roomId, gameInfoUpdateRequest);
	}

	// 게임 종료
	@MessageMapping("/game/{roomId}/finish")
	public void finishGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		gameService.finishGame(memberId, roomId);
	}
}
