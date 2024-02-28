package com.pgms.coredomain.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum SecurityErrorCode implements BaseErrorCode {
	INVALID_TOKEN("sec-400/01", HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
	INVALID_OAUTH_CODE("sec-400/02", HttpStatus.BAD_REQUEST, "유효하지 않은 소셜 로그인 코드입니다."),
	UNAUTHORIZED("sec-401/01", HttpStatus.UNAUTHORIZED, "로그인 해주세요."),
	ACCESS_TOKEN_EXPIRED("sec-401/02", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
	REFRESH_TOKEN_EXPIRED("sec-401/03", HttpStatus.UNAUTHORIZED, "다시 로그인 해주세요."),
	ALREADY_LOGOUT("sec-401/04", HttpStatus.UNAUTHORIZED, "로그아웃 상태로 재로그인이 필요합니다."),
	FORBIDDEN("sec-403/01", HttpStatus.FORBIDDEN, "권한이 없습니다"),
	OAUTH_LOGIN_FAILED("sec-500", HttpStatus.INTERNAL_SERVER_ERROR, "소셜 로그인 중 오류가 발생했습니다. 관리자에게 문의하세요.");

	private final String code;
	private final HttpStatus status;
	private final String message;

	SecurityErrorCode(String code, HttpStatus status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return ErrorResponse.of(code, message);
	}
}
