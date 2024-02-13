package com.pgms.api.domain.member.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record MemberGetResponse(
	Long memberId,
	String email,
	String nickname,
	String status,
	String provider
) {
	public static MemberGetResponse from(Member member) {
		return new MemberGetResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getStatus().name(),
			member.getProviderType().name()
		);
	}
}
