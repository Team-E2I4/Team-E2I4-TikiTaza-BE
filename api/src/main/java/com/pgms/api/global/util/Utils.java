package com.pgms.api.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.api.socket.dto.response.Message;

public class Utils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// JSON -> Java Objcet (deserialization)
	public static Message getObject(final String message) {
		try {
			return objectMapper.readValue(message, Message.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// Java Object -> JSON (serialization)
	public static <T> String getString(final T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
