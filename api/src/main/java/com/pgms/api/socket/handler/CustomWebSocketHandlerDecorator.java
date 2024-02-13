package com.pgms.api.socket.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import com.pgms.api.domain.game.service.GameRoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final GameRoomService gameRoomService;

	private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	public CustomWebSocketHandlerDecorator(WebSocketHandler delegate, GameRoomService gameRoomService) {
		super(delegate);
		this.gameRoomService = gameRoomService;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(">>>> decorator established {} ", session);
		sessionMap.put(session.getId(), session);
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		log.info(">>>> decorator closed {} ", session);
		gameRoomService.exitGameRoom(session.getId());

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
