package com.pgms.coredomain.domain.game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pgms.coredomain.domain.common.BaseEntity;

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

	@Column(name = "host_id", nullable = false)
	private Long hostId;

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

	@Column(name = "is_playing", nullable = false)
	private boolean isPlaying;

	@Column(name = "game_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private GameType gameType;

	@OneToMany(mappedBy = "gameRoom", fetch = FetchType.EAGER)
	@JsonBackReference
	List<GameRoomMember> gameRoomMembers = new ArrayList<>();

	@Builder
	public GameRoom(
		Long hostId,
		String title,
		String password,
		int roundCount,
		int maxPlayer,
		GameType gameType) {
		this.hostId = hostId;
		this.title = title;
		this.password = password;
		this.roundCount = roundCount;
		this.maxPlayer = maxPlayer;
		this.isPlaying = false;
		this.gameType = gameType;
	}

	public void startGame() {
		this.isPlaying = true;
	}

	public boolean isFull() {
		return this.currentPlayer >= this.maxPlayer;
	}

	public boolean isPrivate() {
		return this.password != null;
	}

	public void enterRoom() {
		this.currentPlayer++;
	}

	public void exitRoom() {
		this.currentPlayer--;
	}
}
