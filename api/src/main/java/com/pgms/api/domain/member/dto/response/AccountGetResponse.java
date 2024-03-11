package com.pgms.api.domain.member.dto.response;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coreinfraredis.entity.Guest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "프로필 응답", requiredProperties = {"memberId", "email", "nickname", "ranking", "averageWpm",
	"averageAccuracy", "isGuest"})
public record AccountGetResponse(
	Long memberId,
	String email,
	String nickname,
	Long rank,
	int gameCount,
	double averageCpm,
	double averageAccuracy,
	boolean isGuest
) {
	public static AccountGetResponse from(Member member, Long rank) {
		return new AccountGetResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			rank,
			member.getGameCount(),
			member.getAverageCpm(),
			member.getAverageAccuracy(),
			false
		);
	}

	public static AccountGetResponse from(Guest guest, Long rank) {
		return new AccountGetResponse(
			guest.getId(),
			null,
			guest.getNickname(),
			rank,
			0,
			0,
			0,
			true
		);
	}
}
