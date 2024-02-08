package com.pgms.apigame.exception;

import com.pgms.coredomain.domain.common.GameErrorCode;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {

	private final GameErrorCode errorCode;

	public GameException(GameErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
