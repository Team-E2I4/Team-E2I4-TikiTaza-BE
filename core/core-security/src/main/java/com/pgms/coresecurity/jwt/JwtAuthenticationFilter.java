package com.pgms.coresecurity.jwt;

import static org.springframework.util.StringUtils.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pgms.coredomain.exception.SecurityErrorCode;
import com.pgms.coresecurity.util.HttpResponseUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * jwt 토큰의 존재여부, 유효기간, 형식 검증 후 SecurityContextHolder에 Authentication을 저장해주는 역할
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHENTICATION_HEADER = "Authorization";
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String accessToken = jwtTokenProvider.resolveToken(request.getHeader(AUTHENTICATION_HEADER));
			if (hasText(accessToken)) {
				log.info(">>>>>> AccessToken : {}", accessToken);
				jwtTokenProvider.validateAccessToken(accessToken);
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException e) {
			log.warn(">>>>>> AccessToken is Expired : ", e);
			HttpResponseUtil.writeErrorResponse(response, SecurityErrorCode.ACCESS_TOKEN_EXPIRED);
			return;
		} catch (Exception e) {
			log.warn(">>>>>> Authentication Failed : ", e);
			HttpResponseUtil.writeErrorResponse(response, SecurityErrorCode.INVALID_TOKEN);
			return;
		}
		filterChain.doFilter(request, response);
	}
}
