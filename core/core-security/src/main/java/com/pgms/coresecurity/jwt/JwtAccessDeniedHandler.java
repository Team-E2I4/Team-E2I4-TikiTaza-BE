package com.pgms.coresecurity.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.exception.SecurityErrorCode;
import com.pgms.coresecurity.util.HttpResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		HttpResponseUtil.writeErrorResponse(response, SecurityErrorCode.FORBIDDEN);
	}
}
