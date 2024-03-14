package com.pgms.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "게스트 로그인 응답", requiredProperties = {"accessToken"})
public record GuestResponse(String accessToken) {
	public static GuestResponse from(String accessToken) {
		return new GuestResponse(accessToken);
	}
}
