package com.pgms.coredomain.domain.member;

public enum ProviderType {
	KAKAO,
	GOOGLE;

	public static ProviderType of(String input) {
		try {
			return ProviderType.valueOf(input.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException("존재하지 않는 소셜 로그인입니다. : " + input);
		}
	}
}
