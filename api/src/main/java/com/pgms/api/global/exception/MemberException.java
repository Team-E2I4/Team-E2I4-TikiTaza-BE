package com.pgms.api.global.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends CustomException {

	public MemberException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
