package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum GameRoomErrorCode implements BaseErrorCode {

	VALIDATION_FAILED("gr-400/01", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다."),
	GAME_ROOM_FULL("gr-400/02", HttpStatus.BAD_REQUEST, "방이 꽉 찼습니다."),
	GAME_ROOM_MISMATCH("gr-400/03", HttpStatus.BAD_REQUEST, "방이 일치하지 않습니다."),
	GAME_ROOM_NOT_FOUND("gr-404/01", HttpStatus.NOT_FOUND, "게임방을 찾을 수 없습니다."),
	GAME_ROOM_MEMBER_NOT_FOUND("gr-404/02", HttpStatus.NOT_FOUND, "게임방 멤버를 찾을 수 없습니다."),
	USER_ROOM_LIMIT_EXCEEDED("gr-409/01", HttpStatus.CONFLICT, "이미 해당 유저의 방이 존재합니다.");

	private final String code;
	private final HttpStatus status;
	private final String message;

	GameRoomErrorCode(String code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return ErrorResponse.of(code, message);
	}
}
