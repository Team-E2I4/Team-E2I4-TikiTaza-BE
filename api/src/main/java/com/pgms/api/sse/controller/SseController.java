package com.pgms.api.sse.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "SSE", description = "SSE(Server-Side-Event) API 입니다.")
@RestController
@RequiredArgsConstructor
public class SseController {

	private static final Long EMITTER_EXPIRATION_TIME = 60 * 60 * 1000L;

	private final SseEmitters sseEmitters;
	private final SseService sseService;

	@Operation(summary = "SSE: 게임방 목록 받아오기")
	@GetMapping(value = "/api/v1/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connect() {
		// Emitter 객체 생성 & 5분 시간 설정
		SseEmitter emitter = new SseEmitter(EMITTER_EXPIRATION_TIME);
		sseEmitters.addEmitter(emitter);

		// 2번째 인자로 게임방 리스트 넘겨주면 된다.
		List<GameRoomGetResponse> gameRooms = sseService.getRooms();
		sseEmitters.connect(emitter, gameRooms);

		// 타임아웃 발생시 콜백 등록
		emitter.onTimeout(emitter::complete);

		// 타임아웃 발생시 브라우저에 재요청 연결 보내는데, 이때 새로운 객체 다시 생성하므로 기존의 Emitter 객체 리스트에서 삭제
		emitter.onCompletion(() -> sseEmitters.remove(emitter));

		// 에러 발생시 처리
		emitter.onError(throwable -> emitter.complete());

		return ResponseEntity.ok(emitter);
	}
}
