package com.pgms.api.global.exception.handler;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.api.global.exception.CustomException;
import com.pgms.api.global.util.Utils;
import com.pgms.coredomain.domain.common.BaseErrorCode;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;
import com.pgms.coreinfrakafka.kafka.producer.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalSocketExceptionHandler {

	private final Producer producer;

	@MessageExceptionHandler(CustomException.class)
	public void handleCustomException(CustomException e) {
		final BaseErrorCode errorCode = e.getErrorCode();
		log.warn(">>>>>> Error occurred in Message Mapping: ", e);
		log.warn(">>>>>> errorCode Message{} ", e.getErrorCode().getMessage());
		KafkaMessage kafkaMessage = new KafkaMessage("/from/error", Utils.getString(errorCode.getErrorResponse()));
		producer.produceMessage(kafkaMessage);
	}
}
