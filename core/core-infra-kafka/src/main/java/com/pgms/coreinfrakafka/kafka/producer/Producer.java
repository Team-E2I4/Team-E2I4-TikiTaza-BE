package com.pgms.coreinfrakafka.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pgms.coreinfrakafka.kafka.KafkaMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

	@Value("${spring.kafka.template.default-topic}")
	private String topic = "test_topic";

	private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

	public void produceMessage(KafkaMessage message) {
		log.info(">>>>>> Message = {}", message);
		log.info(">>>>>> Topic = {}", topic);
		kafkaTemplate.send(topic, message);
	}
}
