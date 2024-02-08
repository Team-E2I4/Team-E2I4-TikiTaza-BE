package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record MemberResponse(
	Long memberId,
	String email,
	String nickname,
	String status,
	String provider
) {
	public static MemberResponse from(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getStatus().name(),
			member.getProviderType().name()
		);
	}
}
