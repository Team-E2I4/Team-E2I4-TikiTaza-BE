package com.pgms.api.domain.member.dto.response;

public record AuthResponse(String accessToken, String refreshToken) {

	public static AuthResponse from(String accessToken, String refreshToken) {
		return new AuthResponse(accessToken, refreshToken);
	}
}
