package com.pgms.api.socket.dto.request;

public record GameStartResponse(
	Boolean isGameStarted
) {
	public static GameStartResponse from(Boolean isGameStarted) {
		return new GameStartResponse(isGameStarted);
	}
}
