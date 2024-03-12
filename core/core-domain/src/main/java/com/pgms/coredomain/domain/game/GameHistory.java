package com.pgms.coredomain.domain.game;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "game_history")
public class GameHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "score", nullable = false)
	private long score;

	@Column(name = "cpm", nullable = false)
	private int cpm;

	@Column(name = "accuracy", nullable = false)
	private int accuracy;

	@Enumerated(EnumType.STRING)
	@Column(name = "game_type", nullable = false)
	private GameType gameType;

	@Builder
	public GameHistory(long score, int cpm, int accuracy, GameType gameType, Long memberId) {
		this.score = score;
		this.cpm = cpm;
		this.accuracy = accuracy;
		this.gameType = gameType;
		this.memberId = memberId;
	}
}
