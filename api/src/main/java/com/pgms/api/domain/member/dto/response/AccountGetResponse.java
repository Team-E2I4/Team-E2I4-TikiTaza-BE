package com.pgms.api.domain.member.dto.response;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coreinfraredis.entity.Guest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "프로필 응답", requiredProperties = {"accountId", "email", "nickname", "status"})
public record AccountGetResponse(
	Long accountId,
	String email,
	String nickname,
	boolean isGuest
) {
	public static AccountGetResponse from(Member member) {
		return new AccountGetResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			false
		);
	}

	public static AccountGetResponse from(Guest guest) {
		return new AccountGetResponse(
			guest.getId(),
			null,
			guest.getNickname(),
			true
		);
	}
}
