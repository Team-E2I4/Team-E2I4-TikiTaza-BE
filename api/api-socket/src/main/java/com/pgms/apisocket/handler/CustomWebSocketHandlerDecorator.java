package com.pgms.apisocket.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	public CustomWebSocketHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(">>>> decorator established {} ", session);
		final Long memberId = (Long)session.getAttributes().get("MemberId");
		final String roomId = (String)session.getAttributes().get("RoomId");
		sessionMap.put(memberId + ":" + roomId, session);
		super.afterConnectionEstablished(session);
	}

	// TODO : 브라우저 종료 시, 웹소켓 연결 종료 처리 로직 추후에 필요함
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		log.info(">>>> decorator closed {} ", session);
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
