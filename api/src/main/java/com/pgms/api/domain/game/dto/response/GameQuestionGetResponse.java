package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameQuestion;

public record GameQuestionGetResponse(
	Long id,
	String question
) {
	public static GameQuestionGetResponse of(GameQuestion question) {
		return new GameQuestionGetResponse(question.getId(), question.getQuestion());
	}
}
