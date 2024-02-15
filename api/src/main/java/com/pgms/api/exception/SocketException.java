package com.pgms.api.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class SocketException extends CustomException {

	public SocketException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
