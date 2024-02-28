package com.pgms.api.domain.member.dto.response;

public record KakaoUserGetResponse(String email, String nickname) {
	public static KakaoUserGetResponse from(String email, String nickname) {
		return new KakaoUserGetResponse(email, nickname);
	}
}
