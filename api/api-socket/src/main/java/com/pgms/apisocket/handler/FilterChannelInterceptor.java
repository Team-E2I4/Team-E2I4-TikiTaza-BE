package com.pgms.apisocket.handler;

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
		log.info(">>>>>> headerAccessor : {}", headerAccessor);
		assert headerAccessor != null;
		if (headerAccessor.getCommand() == StompCommand.CONNECT
			|| headerAccessor.getCommand() == StompCommand.SEND) { // 문제 발생 예상 지점
			String token = removeBrackets(String.valueOf(headerAccessor.getNativeHeader("Authorization")));
			token = jwtTokenProvider.resolveToken(token);
			log.info(">>>>>> Token resolved : {}", token);
			try {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				Long memberId = ((UserDetailsImpl)authentication.getPrincipal()).getId();
				headerAccessor.addNativeHeader("MemberId", String.valueOf(memberId));
				log.info(">>>>>> MemberId is set to header : {}", memberId);
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
