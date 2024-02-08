package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum GameErrorCode implements BaseErrorCode {

	GAME_NOT_FOUND("game-404/01", HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다."),
	VALIDATION_FAILED("game-400/01", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다."),
	GAME_ALREADY_DELETED("game-400/02", HttpStatus.BAD_REQUEST, "삭제된 게임입니다."),
	DUPLICATE_GAME_NAME("game-409/01", HttpStatus.CONFLICT, "이미 존재하는 게임 이름입니다.");

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
