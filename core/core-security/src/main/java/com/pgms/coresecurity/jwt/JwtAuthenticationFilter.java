package com.pgms.coresecurity.jwt;

import static org.springframework.util.StringUtils.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pgms.coresecurity.user.normal.UserDetailsImpl;
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

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		log.info(">>>>>>>>>>>>>>>>.. HELLO");
		try {
			String accessToken = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));
			if (hasText(accessToken)) {
				jwtTokenProvider.validateAccessToken(accessToken);
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
				log.info("authentication ={} ", ((UserDetailsImpl)authentication.getPrincipal()).getId());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException e) {
			log.warn(">>>>> AccessToken is Expired : {}", e);
			HttpResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
		} catch (Exception e) {
			log.warn(">>>>> Authentication Failed : {}", e);
			HttpResponseUtil.writeErrorResponse(response, HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다.");
		}
		filterChain.doFilter(request, response);
	}
}
