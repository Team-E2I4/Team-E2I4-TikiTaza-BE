package com.pgms.api.domain.rank.dto;

import com.pgms.coredomain.domain.game.GameRank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "멤버 랭킹 응답", requiredProperties = {
	"memberId", "nickname", "totalScore", "averageWpm", "averageAccuracy"})
public record MemberRankResponse(
	Long memberId,
	String nickname,
	double totalScore,
	double averageWpm,
	double averageAccuracy
) {
	public static MemberRankResponse from(GameRank gameRank) {
		return new MemberRankResponse(
			gameRank.getMemberId(),
			gameRank.getNickName(),
			gameRank.getTotalScore(),
			gameRank.getAverageWpm(),
			gameRank.getAverageAccuracy()
		);
	}
}
