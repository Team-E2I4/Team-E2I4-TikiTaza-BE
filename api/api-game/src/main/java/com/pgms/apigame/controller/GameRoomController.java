package com.pgms.apigame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apigame.dto.GameRoomGetResponse;
import com.pgms.apigame.dto.PageCondition;
import com.pgms.apigame.dto.PageResponse;
import com.pgms.apigame.service.GameRoomService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class GameRoomController {

	private final GameRoomService gameRoomService;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<GameRoomGetResponse>>> getRooms(
		@ModelAttribute @Valid PageCondition pageCondition) {
		return ResponseEntity.ok(ApiResponse.of(gameRoomService.getRooms(pageCondition)));
	}
}
