package com.pgms.api.domain.game.dto.response;

public record GameQuestionGetResponse(String question) {
	public static GameQuestionGetResponse of(String question) {
		return new GameQuestionGetResponse(question);
	}
}
