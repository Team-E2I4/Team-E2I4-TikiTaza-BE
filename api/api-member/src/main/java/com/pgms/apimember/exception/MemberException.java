package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

	private final MemberErrorCode errorCode;

	public MemberException(MemberErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
