package com.pgms.apimember.exception;

import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.coredomain.domain.common.BaseErrorCode;
import com.pgms.coredomain.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MemberGlobalExceptionHandler {

	@ExceptionHandler(MemberException.class)
	protected ResponseEntity<ErrorResponse> handleMemberCustomException(MemberException ex) {
		log.warn(">>>>> Member Custom Exception : {}", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn(">>>>> validation Failed : {}", ex);
		BindingResult bindingResult = ex.getBindingResult();
		String errorMessage = Objects.requireNonNull(bindingResult.getFieldError())
			.getDefaultMessage();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = ErrorResponse.of(VALIDATION_FAILED.getCode(), errorMessage);
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : {}", ex);
		ErrorResponse errorResponse = ErrorResponse.of("mem-500/01", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
