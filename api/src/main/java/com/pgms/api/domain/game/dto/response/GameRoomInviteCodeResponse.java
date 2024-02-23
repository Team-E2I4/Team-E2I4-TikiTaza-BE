package com.pgms.api.domain.game.dto.response;

public record GameRoomInviteCodeResponse(Long roomId) {
	public static GameRoomInviteCodeResponse from(Long roomId) {
		return new GameRoomInviteCodeResponse(roomId);
	}
}
