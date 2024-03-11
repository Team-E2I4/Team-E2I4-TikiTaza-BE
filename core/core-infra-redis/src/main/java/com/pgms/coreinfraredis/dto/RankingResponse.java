package com.pgms.coreinfraredis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "랭킹 응답", requiredProperties = {"nickname", "ranking", "score"})
public class RankingResponse {
	String nickname;
	Long ranking;
	Double score;

	public RankingResponse(String nickname, Long ranking, Double score) {
		this.nickname = nickname;
		this.ranking = ranking != null ? ranking + 1 : -1;
		this.score = score != null ? score : 0.0;
	}
}
