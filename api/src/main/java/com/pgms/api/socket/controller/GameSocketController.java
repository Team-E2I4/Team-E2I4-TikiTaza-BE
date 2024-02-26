package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameService;
import com.pgms.api.socket.dto.request.GameFinishRequest;
import com.pgms.api.socket.dto.request.GameInfoUpdateRequest;
import com.pgms.api.socket.dto.request.WordGameInfoUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameSocketController {

	private final GameService gameService;

	// 사용자가 자신이 게임에 완전히 입장했다고 알리는 엔드포인트
	@MessageMapping("/game/{roomId}/connect")
	public void roundStart(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Member Connected in game : roomId = {}, memberId = {}", roomId, memberId);
		gameService.startFirstRound(roomId);
	}

	// 게임 중 실시간 정보 업데이트 - 문장, 코드
	@MessageMapping("/game/{roomId}/info")
	public void updateGameInfo(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload GameInfoUpdateRequest gameInfoUpdateRequest) {
		log.info(">>>>>> Game Real-Time Info : roomId = {}, memberId = {}", roomId, memberId);
		gameService.updateGameInfo(memberId, roomId, gameInfoUpdateRequest);
	}

	// 게임 중 실시간 정보 업데이트 - 단어
	@MessageMapping("/game/{roomId}/word-info")
	public void updateWordGameInfo(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload WordGameInfoUpdateRequest gameInfoUpdateRequest) {
		log.info(">>>>>> Word Game Real-Time Info : roomId = {}, memberId = {}", roomId, memberId);
		gameService.updateWordGameInfo(memberId, roomId, gameInfoUpdateRequest);
	}

	// 라운드 종료 or 게임 종료
	@MessageMapping("/game/{roomId}/round-finish")
	public void finishGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload GameFinishRequest gameFinishRequest) {
		log.info(">>>>>> Game Finish : roomId = {}, memberId = {}", roomId, memberId);
		gameService.finishGame(roomId, gameFinishRequest);
	}
}
