package com.pgms.api.domain.member.dto.response;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coreinfraredis.entity.Guest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "프로필 응답", requiredProperties = {"memberId", "email", "nickname", "rank", "isGuest"})
public record AccountGetResponse(
	Long memberId,
	String email,
	String nickname,
	int rank,
	boolean isGuest
) {
	public static AccountGetResponse from(Member member, int rank) {
		return new AccountGetResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			rank,
			false
		);
	}

	public static AccountGetResponse from(Guest guest, int rank) {
		return new AccountGetResponse(
			guest.getId(),
			null,
			guest.getNickname(),
			rank,
			true
		);
	}
}
