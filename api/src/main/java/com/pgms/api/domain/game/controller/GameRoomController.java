package com.pgms.api.domain.game.controller;

import static com.pgms.coredomain.response.ResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.game.dto.request.GameRoomCreateRequest;
import com.pgms.api.domain.game.dto.request.GameRoomEnterRequest;
import com.pgms.api.domain.game.dto.request.GameRoomUpdateRequest;
import com.pgms.api.domain.game.dto.response.GameRoomCreateResponse;
import com.pgms.api.domain.game.dto.response.GameRoomEnterResponse;
import com.pgms.api.domain.game.dto.response.GameRoomInviteCodeResponse;
import com.pgms.api.domain.game.service.GameRoomService;
import com.pgms.api.global.annotation.SwaggerResponseGameRoom;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.Account;
import com.pgms.coresecurity.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "게임 방 생성: 방장은 생성 후 자동 입장")
	@PostMapping
	public ResponseEntity<ApiResponse<GameRoomCreateResponse>> createGameRoom(
		@CurrentAccount Account account,
		@RequestBody @Valid GameRoomCreateRequest request) {
		final GameRoomCreateResponse response = gameRoomService.createGameRoom(account, request);
		return ResponseEntity.ok(ApiResponse.of(CREATE, response));
	}

	@Operation(summary = "게임 방 설정 변경")
	@PatchMapping("/{roomId}")
	public ResponseEntity<ApiResponse<Void>> updateGameRoom(
		@CurrentAccount Account account,
		@PathVariable Long roomId,
		@RequestBody @Valid GameRoomUpdateRequest request) {
		gameRoomService.updateRoom(account, roomId, request);
		return ResponseEntity.ok(ApiResponse.of(SUCCESS));
	}

	@Operation(summary = "게임 방 입장: 일반 유저 입장")
	@PostMapping("/{roomId}/enter")
	public ResponseEntity<ApiResponse<GameRoomEnterResponse>> enterGameRoom(
		@CurrentAccount Account account,
		@PathVariable Long roomId,
		@RequestBody(required = false) GameRoomEnterRequest request) {
		return ResponseEntity.ok(ApiResponse.of(gameRoomService.enterGameRoom(account, roomId, request)));
	}

	@Operation(summary = "게임방 초대 코드로 방 번호 반환")
	@GetMapping
	public ResponseEntity<ApiResponse<GameRoomInviteCodeResponse>> getRoomIdByInviteCode(
		@CurrentAccount Account account,
		@RequestParam String inviteCode) {
		return ResponseEntity.ok(ApiResponse.of(gameRoomService.getRoomIdByInviteCode(account, inviteCode)));
	}
}
