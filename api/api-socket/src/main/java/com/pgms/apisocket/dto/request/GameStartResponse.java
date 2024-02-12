package com.pgms.apisocket.dto.request;

import lombok.ToString;

@ToString
public record GameStartResponse(
	Boolean isGameStarted
) {
	public static GameStartResponse from(Boolean isGameStarted) {
		return new GameStartResponse(isGameStarted);
	}
}
