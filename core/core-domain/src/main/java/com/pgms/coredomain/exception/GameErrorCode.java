package com.pgms.coredomain.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum GameErrorCode implements BaseErrorCode {

	GAME_ALREADY_STARTED("gr-400/01", HttpStatus.BAD_REQUEST, "이미 시작된 게임입니다.");

	private final String code;
	private final HttpStatus status;
	private final String message;

	GameErrorCode(String code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return ErrorResponse.of(code, message);
	}
}
