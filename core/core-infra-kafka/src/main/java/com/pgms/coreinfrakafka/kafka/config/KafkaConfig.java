package com.pgms.coreinfrakafka.kafka.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.pgms.coreinfrakafka.kafka.KafkaMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Bean
	public ProducerFactory<String, KafkaMessage> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		// kafka 서버가 실행되는 포트
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		// Producer가 key 값의 데이터를 Kafka 브로커로 전송하기 전에 데이터를 Byte Array로 변환하는데 사용하는 직렬화 매커니즘 설정, key 값은 String이므로 String으로 직렬화
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Message 객체를 브로커로 전달하기 때문에 Json으로 직렬화
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, KafkaMessage> consumerFactory() {
		HashMap<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, getConsumerGroupId());
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
			new JsonDeserializer<>(KafkaMessage.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	private String getConsumerGroupId() {
		// 현재 시간 가져오기 (UTC)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String currentTime = LocalDateTime.now().format(formatter);

		// 호스트 이름 추출
		String hostName = "unknown-host";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error("Failed to get host name", e);
		}

		// 컨슈머 그룹 ID 생성
		String consumerGroupId = "consumer-" + hostName + "-" + currentTime;
		log.info("Kafka Consumer Group ID: {}", consumerGroupId);
		return consumerGroupId;
	}
}
