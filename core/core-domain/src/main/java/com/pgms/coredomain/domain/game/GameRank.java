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
@Table(name = "game_rank")
public class GameRank extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "total_score")
	private double totalScore;

	@Column(name = "average_wpm")
	private double averageWpm;

	@Column(name = "average_accuracy")
	private double averageAccuracy;

	@Column(name = "game_type")
	@Enumerated(EnumType.STRING)
	private GameType gameType;

	@Builder
	public GameRank(Long memberId, String nickname, double totalScore, double averageWpm, double averageAccuracy,
		GameType gameType) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.totalScore = totalScore;
		this.averageWpm = averageWpm;
		this.averageAccuracy = averageAccuracy;
		this.gameType = gameType;
	}
}
