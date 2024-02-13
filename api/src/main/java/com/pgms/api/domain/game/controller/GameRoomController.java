package com.pgms.api.domain.game.controller;

import static com.pgms.coredomain.response.ResponseCode.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.api.domain.game.dto.PageCondition;
import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.domain.game.dto.response.PageResponse;
import com.pgms.api.domain.game.service.GameRoomService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.CurrentAccount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class GameRoomController {

	private final GameRoomService gameRoomService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createRoom(
		@CurrentAccount Long memberId,
		@RequestBody @Valid GameRoomCreateRequest request) {
		final Long roomId = gameRoomService.createRoom(memberId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(roomId)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.of(CREATE, roomId));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<GameRoomGetResponse>>> getRooms(
		@ModelAttribute @Valid PageCondition pageCondition) {
		return ResponseEntity.ok(ApiResponse.of(gameRoomService.getRooms(pageCondition)));
	}
}
