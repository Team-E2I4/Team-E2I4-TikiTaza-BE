package com.pgms.coresecurity.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.coredomain.exception.BaseErrorCode;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coredomain.response.ErrorResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HttpResponseUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
		throws IOException {
		String responseBody = objectMapper.writeValueAsString(ApiResponse.of(body));
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(httpStatus.value());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}

	public static void writeErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws
		IOException {
		final ErrorResponse errorResponse = errorCode.getErrorResponse();
		String responseBody = objectMapper.writeValueAsString(errorResponse);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(errorCode.getStatus().value());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}
}
