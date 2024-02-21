package com.pgms.api.global.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.coredomain.exception.BaseErrorCode;
import com.pgms.coredomain.exception.CustomException;
import com.pgms.coredomain.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		log.warn(">>>>> Custom Exception : ", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn(">>>>> validation Failed : ", ex);
		BindingResult bindingResult = ex.getBindingResult();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = ErrorResponse.of("400", "입력값에 대한 검증에 실패했습니다.");
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : ", ex);
		ErrorResponse errorResponse = ErrorResponse.of("500", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
