package com.pgms.coredomain.domain.game;

import lombok.Getter;

@Getter
public enum GameType {
	SENTENCE("문장 따라치기"),
	CODE("코드 따라치기"),
	WORD("짧은 단어");

	private final String description;

	GameType(String description) {
		this.description = description;
	}

	public static GameType of(String input) {
		try {
			return GameType.valueOf(input.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException("존재하지 않는 게임 모드입니다. : " + input);
		}
	}
}
