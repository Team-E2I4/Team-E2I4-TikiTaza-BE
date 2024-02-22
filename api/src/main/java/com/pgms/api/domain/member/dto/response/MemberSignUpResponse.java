package com.pgms.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "회원가입 응답", requiredProperties = {"memberId"})
public record MemberSignUpResponse(Long memberId) {
	public static MemberSignUpResponse from(Long memberId) {
		return new MemberSignUpResponse(memberId);
	}
}
