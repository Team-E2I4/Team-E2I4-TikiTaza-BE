package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum SocketErrorCode implements BaseErrorCode {

	SOCKET_ERROR("sock-400/01", HttpStatus.NOT_FOUND, "소켓 에러가 발생했습니다.");

	private final String code;
	private final HttpStatus status;
	private final String message;

	SocketErrorCode(String code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return ErrorResponse.of(code, message);
	}
}
