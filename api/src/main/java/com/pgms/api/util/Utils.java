package com.pgms.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.api.socket.dto.Message;

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
	public static String getString(final Message message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
