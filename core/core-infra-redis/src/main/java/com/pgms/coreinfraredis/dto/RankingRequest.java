package com.pgms.coreinfraredis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RankingRequest {
	final String nickname;
	final Double score;
	String gameType;

	@Builder
	public RankingRequest(String nickname, Double score, String gameType) {
		this.nickname = nickname;
		this.score = score;
		this.gameType = gameType;
	}
}
