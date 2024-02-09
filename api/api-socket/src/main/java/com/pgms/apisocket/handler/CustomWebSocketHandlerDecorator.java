package com.pgms.apisocket.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import com.pgms.coresecurity.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	public CustomWebSocketHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		final Long memberId = SecurityUtil.getCurrentAccountId();
		final String roomId = (String)session.getAttributes().get("roomId");
		sessionMap.put(memberId + ":" + roomId, session);
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		sessionMap.values().remove(session);
		super.afterConnectionClosed(session, closeStatus);
	}

	public void closeSession(String sessionId) {
		WebSocketSession session = sessionMap.get(sessionId);
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				log.error(">>>>>> Failed to close session", e);
			}
		}
	}
}
