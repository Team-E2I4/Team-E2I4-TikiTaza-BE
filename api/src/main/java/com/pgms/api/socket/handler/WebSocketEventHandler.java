package com.pgms.api.socket.handler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.pgms.api.domain.game.service.GameRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

	private final GameRoomService gameRoomService;
	private SimpMessageSendingOperations messagingTemplate;
	private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	// 연결이 성립되었을 때의 이벤트 처리
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		final String sessionId = headerAccesor.getSessionId();
		log.info(">>>>>> Connected : {}", sessionId);
	}

	// 연결이 종료되었을 때의 이벤트 처리
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		String memberId = (String)Objects.requireNonNull(headerAccesor.getSessionAttributes()).get("MemberId");
		log.info(">>>>>> Disconnected : {}", sessionId);
		log.info(">>>>>> Disconnected : {}", memberId);

		gameRoomService.exitGameRoom(sessionId);
	}
}
