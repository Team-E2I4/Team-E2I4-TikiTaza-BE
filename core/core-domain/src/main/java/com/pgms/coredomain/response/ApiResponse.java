package com.pgms.coredomain.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "공통 응답 형식", requiredProperties = {"code", "message"})
public class ApiResponse<T> {

	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	private ApiResponse(String code, String message, T data) {
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
