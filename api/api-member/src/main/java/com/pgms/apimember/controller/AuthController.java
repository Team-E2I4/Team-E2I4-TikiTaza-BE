package com.pgms.apimember.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.response.AuthResponse;
import com.pgms.apimember.service.AuthService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(ApiResponse.of(authService.login(request)));
	}

	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<AuthResponse>> reIssueAccessToken(
		@CookieValue("refreshToken") String refreshToken) {
		AuthResponse response = authService.reIssueAccessTokenByRefresh(refreshToken);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
