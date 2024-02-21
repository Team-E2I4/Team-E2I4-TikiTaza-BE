package com.pgms.api.global.exception;

import com.pgms.coredomain.exception.BaseErrorCode;
import com.pgms.coredomain.exception.CustomException;

import lombok.Getter;

@Getter
public class GameException extends CustomException {

	public GameException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
