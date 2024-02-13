package com.pgms.apisocket.dto.request;

public record GameStartResponse(
	Boolean isGameStarted
) {
	public static GameStartResponse from(Boolean isGameStarted) {
		return new GameStartResponse(isGameStarted);
	}
}
