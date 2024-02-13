package com.pgms.api.domain.member.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public MemberException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
