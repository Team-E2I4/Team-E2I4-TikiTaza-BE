package com.pgms.coredomain.domain.game;

import com.pgms.coredomain.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private GameRoom gameRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;
}
