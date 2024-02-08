package com.pgms.coresecurity.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class SecurityException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public SecurityException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
