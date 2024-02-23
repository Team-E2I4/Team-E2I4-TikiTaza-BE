package com.pgms.coredomain.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum
GameRoomErrorCode implements BaseErrorCode {

	VALIDATION_FAILED("gr-400/01", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다."),
	GAME_ROOM_FULL("gr-400/02", HttpStatus.BAD_REQUEST, "방이 꽉 찼습니다."),
	GAME_ROOM_MISMATCH("gr-400/03", HttpStatus.BAD_REQUEST, "방이 일치하지 않습니다."),
	GAME_ROOM_PASSWORD_MISMATCH("gr-400/04", HttpStatus.BAD_REQUEST, "방 비밀번호가 일치하지 않습니다."),
	INVALID_GAME_TYPE("gr-400/05", HttpStatus.BAD_REQUEST, "유효하지 않은 게임 타입입니다."),
	GAME_ROOM_NOT_FOUND("gr-404/01", HttpStatus.NOT_FOUND, "게임방을 찾을 수 없습니다."),
	GAME_ROOM_MEMBER_NOT_FOUND("gr-404/02", HttpStatus.NOT_FOUND, "게임방 멤버를 찾을 수 없습니다."),
	GAME_INFO_NOT_FOUND("gr-404/03", HttpStatus.NOT_FOUND, "게임 정보를 찾을 수 없습니다."),
	USER_ROOM_LIMIT_EXCEEDED("gr-409/01", HttpStatus.CONFLICT, "이미 해당 유저의 방이 존재합니다."),
	GAME_ROOM_HOST_MISMATCH("gr-409/02", HttpStatus.CONFLICT, "방장이 아닙니다."),
	GAME_ROOM_MAX_PLAYER_MISMATCH("gr-409/03", HttpStatus.CONFLICT, "방 최대 인원이 현재 인원보다 작습니다."),
	GAME_ROOM_MEMBER_IN_SAME_ROOM("gr-409/04", HttpStatus.CONFLICT, "같은 방에 이미 참여하고 있습니다.");

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
