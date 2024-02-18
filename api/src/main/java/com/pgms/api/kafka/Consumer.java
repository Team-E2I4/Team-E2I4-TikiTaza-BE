package com.pgms.api.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.pgms.api.socket.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

	private final SimpMessageSendingOperations messageSendingOperations;

	@KafkaListener(topics = "${spring.kafka.template.default-topic}")
	public void consume(Message message) {
		log.info(">>>>>> Consume !!!!!!!");
		messageSendingOperations.convertAndSend(message.getDestination(), message);
	}
}
