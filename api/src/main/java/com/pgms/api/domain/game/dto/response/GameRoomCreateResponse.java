package com.pgms.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "게임방 생성 응답", requiredProperties = {"roomId"})
public record GameRoomCreateResponse(Long roomId) {
	public static GameRoomCreateResponse from(Long roomId) {
		return new GameRoomCreateResponse(roomId);
	}
}
