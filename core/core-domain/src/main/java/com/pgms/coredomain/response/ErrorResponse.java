package com.pgms.coredomain.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

	private final String errorCode;
	private final String errorMessage;
	private final Map<String, String> validation = new HashMap<>();

	private ErrorResponse(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public static ErrorResponse of(String errorCode, String errorMessage) {
		return new ErrorResponse(errorCode, errorMessage);
	}

	public void addValidation(String field, String message) {
		validation.put(field, message);
	}
}
