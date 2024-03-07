package com.pgms.api.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.member.dto.request.LoginRequest;
import com.pgms.api.domain.member.dto.response.AuthResponse;
import com.pgms.api.domain.member.service.AuthService;
import com.pgms.api.global.annotation.SwaggerResponseAuth;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coredomain.response.ResponseCode;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.resolver.Account;
import com.pgms.coresecurity.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 인증", description = "회원 인증 관련 API 입니다.")
@SwaggerResponseAuth
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final String SET_COOKIE = "Set-Cookie";

	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		final AuthResponse response = authService.login(request);
		return ResponseEntity.ok()
			.header(SET_COOKIE, getRefreshTokenHeader(response.refreshToken()))
			.body(ApiResponse.of(response));
	}

	@Operation(summary = "게스트 로그인")
	@PostMapping("/guest")
	public ResponseEntity<ApiResponse<AuthResponse>> guestLogin() {
		final AuthResponse response = authService.guestLogin();
		return ResponseEntity.ok()
			.header(SET_COOKIE, getRefreshTokenHeader(response.refreshToken()))
			.body(ApiResponse.of(response));
	}

	@Operation(summary = "카카오 로그인")
	@PostMapping("/login/kakao")
	public ResponseEntity<ApiResponse<AuthResponse>> kakaoLogin(@RequestParam("code") String code) {
		AuthResponse response = authService.kakaoLogin(code);
		return ResponseEntity.ok()
			.header(SET_COOKIE, getRefreshTokenHeader(response.refreshToken()))
			.body(ApiResponse.of(response));
	}

	@Operation(summary = "로그아웃")
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(
		@RequestHeader("Authorization") String bearerToken,
		@CookieValue("refreshToken") String refreshToken,
		@CurrentAccount Account account) {
		authService.logout(jwtTokenProvider.resolveToken(bearerToken), refreshToken, account.id());
		return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
	}

	@Operation(summary = "토큰 재발급")
	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<AuthResponse>> reIssueAccessToken(
		@Parameter(hidden = true) @CookieValue(value = "refreshToken", required = false) String refreshToken) {
		final AuthResponse response = authService.reIssueAccessTokenByRefresh(refreshToken);
		return ResponseEntity.ok()
			.header(SET_COOKIE, getRefreshTokenHeader(response.refreshToken()))
			.body(ApiResponse.of(response));
	}

	private String getRefreshTokenHeader(String refreshToken) {
		return "refreshToken=" + refreshToken + "; Path=/; HttpOnly; SameSite=None";
	}
}
