package com.pgms.api.socket.dto;

import com.pgms.coredomain.domain.game.GameQuestion;

public record GameQuestionGetResponse(
	String question
) {
	public static GameQuestionGetResponse of(GameQuestion question) {
		return new GameQuestionGetResponse(question.getQuestion());
	}
}
