package com.pgms.api.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class GameException extends CustomException {

	public GameException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
