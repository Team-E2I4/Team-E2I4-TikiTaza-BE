package com.pgms.coresecurity.user.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.pgms.coredomain.domain.member.Member;

public class CustomOAuth2User extends DefaultOAuth2User {

	private final Member member;

	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
		String nameAttributeKey, Member member) {
		super(authorities, attributes, nameAttributeKey);
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
