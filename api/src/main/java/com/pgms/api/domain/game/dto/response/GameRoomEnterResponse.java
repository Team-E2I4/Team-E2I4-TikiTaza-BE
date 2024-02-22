package com.pgms.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "게임방 입장 응답", requiredProperties = {"roomId", "memberId"})
public record GameRoomEnterResponse(Long roomId, Long memberId) {
	public static GameRoomEnterResponse from(Long roomId, Long memberId) {
		return new GameRoomEnterResponse(roomId, memberId);
	}
}
