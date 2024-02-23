package com.pgms.coredomain.domain.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GameHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "score")
	private int score;

	@Column(name = "wpm")
	private int wpm;

	@Column(name = "accuracy")
	private int accuracy;

	@Enumerated(EnumType.STRING)
	@Column(name = "game_type")
	private GameType gameType;

	@Column(name = "member_id")
	private Long memberId;

	@Builder
	public GameHistory(int score, int wpm, int accuracy, GameType gameType, Long memberId) {
		this.score = score;
		this.wpm = wpm;
		this.accuracy = accuracy;
		this.gameType = gameType;
		this.memberId = memberId;
	}
}
