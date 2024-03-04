package com.pgms.coredomain.domain.game;

import com.pgms.coredomain.exception.CustomException;
import com.pgms.coredomain.exception.GameRoomErrorCode;

import lombok.Getter;

@Getter
public enum GameType {
	SENTENCE("문장 따라치기", 10),
	CODE("코드 따라치기", 1),
	WORD("짧은 단어", 100);

	private final String description;
	private final int questionCount;

	GameType(String description, int questionCount) {
		this.description = description;
		this.questionCount = questionCount;
	}

	public static GameType of(String input) {
		try {
			return GameType.valueOf(input.toUpperCase());
		} catch (Exception e) {
			throw new CustomException(GameRoomErrorCode.INVALID_GAME_TYPE);
		}
	}
}
