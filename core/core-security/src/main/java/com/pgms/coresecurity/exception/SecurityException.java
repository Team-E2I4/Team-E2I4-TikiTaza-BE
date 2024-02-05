package com.pgms.coresecurity.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

public class SecurityException extends RuntimeException {

	private final MemberErrorCode memberErrorCode;

	public SecurityException(MemberErrorCode memberErrorCode) {
		this.memberErrorCode = memberErrorCode;
	}
}
