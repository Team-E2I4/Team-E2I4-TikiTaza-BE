package com.pgms.api.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "로그인 응답", requiredProperties = {"accessToken"})
public record AuthResponse(
	String accessToken,

	@JsonIgnore
	@Schema(hidden = true)
	String refreshToken) {

	public static AuthResponse from(String accessToken, String refreshToken) {
		return new AuthResponse(accessToken, refreshToken);
	}
}
