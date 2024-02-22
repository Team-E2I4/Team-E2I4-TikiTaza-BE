package com.pgms.api.domain.member.dto.response;

import com.pgms.coredomain.domain.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "멤버 프로필 응답", requiredProperties = {"memberId", "email", "nickname", "status"})
public record MemberGetResponse(
	Long memberId,
	String email,
	String nickname,
	String status

) {
	public static MemberGetResponse from(Member member) {
		return new MemberGetResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getStatus().name()
		);
	}
}
