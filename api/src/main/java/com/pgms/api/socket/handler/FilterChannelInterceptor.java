package com.pgms.api.socket.handler;

import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		log.info(">>>>> 웹소켓 메시지 헤더 정보 : {}", headerAccessor);
		assert headerAccessor != null;
		if (Objects.equals(headerAccessor.getCommand(), StompCommand.CONNECT)
			|| Objects.equals(headerAccessor.getCommand(), StompCommand.SEND)) { // 문제 발생 예상 지/점
			String accessToken = removeBrackets(String.valueOf(headerAccessor.getNativeHeader("Authorization")));
			accessToken = jwtTokenProvider.resolveToken(accessToken);
			log.info(">>>>>> 웹소켓 AccessToken : {}", accessToken);
			try {
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
				Long accountId = ((UserDetailsImpl)authentication.getPrincipal()).getId();
				headerAccessor.addNativeHeader("AccountId", String.valueOf(accountId));
				log.info(">>>>>> 웹소켓 AccountId : {}", accountId);
			} catch (Exception e) {
				log.warn(">>>>> Authentication Failed in FilterChannelInterceptor : ", e);
			}
		}
		return message;
	}

	private String removeBrackets(String token) {
		if (token.startsWith("[") && token.endsWith("]")) {
			return token.substring(1, token.length() - 1);
		}
		return token;
	}
}
