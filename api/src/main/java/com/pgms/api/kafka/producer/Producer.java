package com.pgms.api.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.pgms.api.socket.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

	private final KafkaTemplate<String, Message> kafkaTemplate;

	public void produceMessage(Message message) {
		log.info(">>>>>> Message = {}", message);
		kafkaTemplate.send("kafkaTest", message);
	}
}
