package com.pgms.apisocket.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class SocketException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public SocketException(BaseErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
