package com.pgms.coresecurity.user.oauth;

import java.util.Map;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.ProviderType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String email;
	private String provider;

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		if (registrationId.equals("kakao")) {
			return ofKakao("id", attributes);
		}
		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");

		return OAuthAttributes.builder()
			.email((String)response.get("email"))
			.attributes(response)
			.provider("kakao")
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.provider("google")
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public Member toEntity() {
		return Member.builder()
			.providerType(ProviderType.of(provider))
			.email(email)
			.build();
	}
}
