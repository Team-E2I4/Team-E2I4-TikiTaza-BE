package com.pgms.coredomain.domain.game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pgms.coredomain.domain.common.BaseEntity;
import com.pgms.coredomain.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * GameRoom (게임방) : 초대 코드, 현재 인원
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GameRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "owner_id")
	private Long ownerId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "password")
	private String password;

	@Column(name = "invite_code")
	private String inviteCode;

	@Column(name = "round_count", nullable = false)
	private int roundCount;

	@Column(name = "current_player", nullable = false)
	private int currentPlayer;

	@Column(name = "max_player", nullable = false)
	private int maxPlayer;

	@Column(name = "is_started", nullable = false)
	private boolean isStarted;

	@Column(name = "game_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private GameType gameType;

	@OneToMany(mappedBy = "gameRoom", fetch = FetchType.EAGER)
	@JsonBackReference
	List<Member> members = new ArrayList<>();

	@Builder
	public GameRoom(
		Long ownerId,
		String title,
		String password,
		int roundCount,
		int maxPlayer,
		boolean isStarted,
		GameType gameType) {
		this.ownerId = ownerId;
		this.title = title;
		this.password = password;
		this.roundCount = roundCount;
		this.maxPlayer = maxPlayer;
		this.isStarted = isStarted;
		this.gameType = gameType;
	}

	public void enterGameRoom(Member member) {
		if (member.getGameRoom() != null) // 이미 방에 들어가있는 경우
			member.getGameRoom().exitGameRoom(member); // 기존 방을 가져와서 나가기
		member.setGameRoom(this);
		this.members.add(member);
		this.currentPlayer++;
	}

	public void exitGameRoom(Member member) {
		this.members.remove(member);
		member.setGameRoom(null);
		this.currentPlayer--;
	}

	public void startGame() {
		this.isStarted = true;
	}

	public boolean isFull() {
		return this.currentPlayer >= this.maxPlayer;
	}

	public boolean isPrivate() {
		return this.password != null;
	}
}
