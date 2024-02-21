package com.pgms.coredomain.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

public interface BaseErrorCode {
	ErrorResponse getErrorResponse();

	String getMessage();

	HttpStatus getStatus();
}
