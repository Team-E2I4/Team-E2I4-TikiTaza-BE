package com.pgms.api.domain.game.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public GameException(BaseErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
