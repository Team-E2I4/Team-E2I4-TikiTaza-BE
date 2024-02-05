package com.pgms.coredomain.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

	SUCCESS(200, "정상 처리되었습니다."),
	CREATE(201, "정상적으로 생성되었습니다.");

	private final int code;
	private final String message;

	ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
