package com.pgms.api.global.exception;

import com.pgms.coredomain.exception.BaseErrorCode;
import com.pgms.coredomain.exception.CustomException;

import lombok.Getter;

@Getter
public class MemberException extends CustomException {

	public MemberException(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
