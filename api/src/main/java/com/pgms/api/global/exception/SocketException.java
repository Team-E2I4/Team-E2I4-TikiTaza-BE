package com.pgms.api.global.exception;

import com.pgms.coredomain.exception.BaseErrorCode;
import com.pgms.coredomain.exception.CustomException;

import lombok.Getter;

@Getter
public class SocketException extends CustomException {

	private final Long roomId;

	public SocketException(Long roomId, BaseErrorCode errorCode) {
		super(errorCode);
		this.roomId = roomId;
	}
}
