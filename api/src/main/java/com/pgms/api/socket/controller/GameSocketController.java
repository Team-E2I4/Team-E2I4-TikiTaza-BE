package com.pgms.api.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.service.GameService;
import com.pgms.api.socket.controller.dto.WordGameInfoUpdateRequest;
import com.pgms.api.socket.dto.GameFinishRequest;
import com.pgms.api.socket.dto.GameInfoUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameSocketController {

	private final GameService gameService;

	// 게임방에서 시작 메시지 처리
	@MessageMapping("/game/{roomId}/start")
	public void startGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Game Start : roomId = {}, memberId = {}", roomId, memberId);
		gameService.startGame(roomId, memberId);
	}

	// 사용자가 자신이 게임에 완전히 입장했다고 알리는 엔드포인트
	// 전부 입장 시 /from/game/{roomId}/round-start 로 게임 문제를 전송
	@MessageMapping("/game/{roomId}/round-start")
	public void roundStart(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId) {
		log.info(">>>>>> Game Round Start : roomId = {}, memberId = {}", roomId, memberId);
		gameService.roundStart(roomId);
	}

	// 게임 중 실시간 정보 업데이트
	@MessageMapping("/game/{roomId}/info")
	public void updateGameInfo(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload GameInfoUpdateRequest gameInfoUpdateRequest) {
		log.info(">>>>>> Game Real-Time Info : roomId = {}, memberId = {}", roomId, memberId);
		gameService.updateGameInfo(memberId, roomId, gameInfoUpdateRequest);
	}

	// 게임 중 실시간 정보 업데이트
	@MessageMapping("/game/{roomId}/word-info")
	public void updateWordGameInfo(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload WordGameInfoUpdateRequest gameInfoUpdateRequest) {
		log.info(">>>>>> Game Real-Time Info : roomId = {}, memberId = {}", roomId, memberId);
		gameService.updateWordGameInfo(memberId, roomId, gameInfoUpdateRequest);
	}

	// 게임 라운드 종료
	// 마지막 라운드 체크 ->
	// 마지막 라운드 : /from/game/{roomId}/round-finish 로 전송
	// 마지막 라운드 x: /from/game/{roomId}/round-start로 문제 전송
	@MessageMapping("/game/{roomId}/round-finish")
	public void finishGame(
		@Header("MemberId") Long memberId,
		@DestinationVariable Long roomId,
		@Payload GameFinishRequest gameFinishRequest) {
		log.info(">>>>>> Game Finish : roomId = {}, memberId = {}", roomId, memberId);
		gameService.finishGame(roomId, gameFinishRequest);
	}
}
