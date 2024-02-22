package com.pgms.api.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AuthResponse(
	String accessToken,
	@JsonIgnore
	String refreshToken) {

	public static AuthResponse from(String accessToken, String refreshToken) {
		return new AuthResponse(accessToken, refreshToken);
	}
}
