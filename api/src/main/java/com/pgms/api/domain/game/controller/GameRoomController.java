package com.pgms.api.domain.game.controller;

import static com.pgms.coredomain.response.ResponseCode.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.service.GameRoomService;
import com.pgms.api.global.annotation.SwaggerResponseGameRoom;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "게임방", description = "게임방 관련 API 입니다.")
@SwaggerResponseGameRoom
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class GameRoomController {

	private final GameRoomService gameRoomService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createGameRoom(
		@CurrentAccount Long memberId,
		@RequestBody @Valid GameRoomCreateRequest request) {
		final Long roomId = gameRoomService.createRoom(memberId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(roomId)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.of(CREATE, roomId));
	}

	@PostMapping("/{roomId}/enter")
	public ResponseEntity<ApiResponse<Long>> enterGameRoom(@CurrentAccount Long memberId, @PathVariable Long roomId) {
		return ResponseEntity.ok(ApiResponse.of(gameRoomService.enterGameRoom(memberId, roomId)));
	}
}
