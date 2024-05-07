package com.pgms.coreinfrakafka.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

	private final SimpMessageSendingOperations messageSendingOperations;

	@KafkaListener(topics = "${spring.kafka.template.default-topic}")
	public void consume(KafkaMessage message) {
		log.info(">>>>>> Consume !!!!!!! Message {} ", message);
		messageSendingOperations.convertAndSend(message.destination(), message.message());
	}
}
