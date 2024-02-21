package com.pgms.api.global.exception.handler;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.api.global.exception.SocketException;
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

	@MessageExceptionHandler(SocketException.class)
	public void handleSocketException(SocketException e) {
		final BaseErrorCode errorCode = e.getErrorCode();
		log.warn(">>>>>> Error occurred in Message Mapping: ", e);
		log.warn(">>>>>> errorCode Message{} ", e.getErrorCode().getMessage());
		KafkaMessage kafkaMessage = new KafkaMessage("/from/game-room/%d/error".formatted(e.getRoomId()),
			Utils.getString(errorCode.getErrorResponse()));
		producer.produceMessage(kafkaMessage);
	}
}
