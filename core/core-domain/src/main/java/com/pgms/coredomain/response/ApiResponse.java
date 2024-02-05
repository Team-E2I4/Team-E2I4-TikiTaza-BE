package com.pgms.coredomain.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private final int code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	private ApiResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(T data) {
		return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
	}

	public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
		return new ApiResponse<>(responseCode.getCode(), responseCode.getMessage(), data);
	}

	public static <T> ApiResponse<T> of(ResponseCode responseCode) {
		return new ApiResponse<>(responseCode.getCode(), responseCode.getMessage(), null);
	}
}
