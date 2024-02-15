package com.pgms.api.exception.handler;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.api.exception.CustomException;
import com.pgms.api.util.Utils;
import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalSocketExceptionHandler {

	private final SimpMessageSendingOperations sendingOperations;

	@MessageExceptionHandler(CustomException.class)
	public void handleCustomException(CustomException e) {
		final BaseErrorCode errorCode = e.getErrorCode();
		log.warn(">>>>>> Error occurred in Message Mapping: ", e);
		sendingOperations.convertAndSend("/from/error", Utils.getString(errorCode.getErrorResponse()));
	}
}
