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
import jakarta.persistence.Table;
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
@Table(name = "game_room")
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

	@Column(name = "round", nullable = false)
	private int round;

	@Column(name = "current_player", nullable = false)
	private int currentPlayer;

	@Column(name = "max_player", nullable = false)
	private int maxPlayer;

	@Column(name = "is_playing", nullable = false)
	private boolean isPlaying;

	@Column(name = "game_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private GameType gameType;

	@JsonBackReference
	@OneToMany(mappedBy = "gameRoom", fetch = FetchType.EAGER)
	List<GameRoomMember> gameRoomMembers = new ArrayList<>();

	@Builder
	public GameRoom(
		Long hostId,
		String title,
		String password,
		String inviteCode,
		int round,
		int maxPlayer,
		GameType gameType) {
		this.hostId = hostId;
		this.title = title;
		this.password = password;
		this.inviteCode = inviteCode;
		this.round = round;
		this.maxPlayer = maxPlayer;
		this.isPlaying = false;
		this.gameType = gameType;
	}

	public void updateGameRoom(String title, String password, int round, int maxPlayer, GameType gameType) {
		this.title = title;
		this.password = password;
		this.maxPlayer = maxPlayer;
		this.round = round;
		this.gameType = gameType;
	}

	public void updateGameRoomStatus(boolean status) {
		this.isPlaying = status;
	}

	public void updateHostId(Long memberId) {
		this.hostId = memberId;
	}

	public boolean isFull() {
		return this.currentPlayer >= this.maxPlayer;
	}

	public boolean isEmpty() {
		return this.currentPlayer == 0;
	}

	public boolean isPrivate() {
		return this.password != null;
	}

	public boolean isAllReady() {
		return gameRoomMembers.stream().allMatch(GameRoomMember::isReadyStatus);
	}

	public boolean isHost(Long memberId) {
		return this.hostId.equals(memberId);
	}

	public void enterRoom() {
		this.currentPlayer++;
	}

	public void exitRoom() {
		this.currentPlayer--;
	}
}
