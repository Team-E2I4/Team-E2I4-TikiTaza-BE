package com.pgms.api.domain.rank.dto;

public record MemberRankResponse(
	Long memberId,
	String nickname,
	double totalScore,
	double averageWpm,
	double averageAccuracy
) {
}
