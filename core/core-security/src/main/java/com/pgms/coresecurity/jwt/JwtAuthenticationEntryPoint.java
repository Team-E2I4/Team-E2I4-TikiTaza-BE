package com.pgms.coresecurity.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.exception.SecurityErrorCode;
import com.pgms.coresecurity.util.HttpResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		HttpResponseUtil.writeErrorResponse(response, SecurityErrorCode.UNAUTHORIZED);
	}
}
