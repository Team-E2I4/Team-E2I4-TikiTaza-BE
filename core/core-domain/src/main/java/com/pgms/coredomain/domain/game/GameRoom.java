package com.pgms.coredomain.domain.game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pgms.coredomain.domain.common.BaseEntity;
import com.pgms.coredomain.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "invite_code", nullable = false)
	private String inviteCode;

	@Column(name = "round_count", nullable = false)
	private Integer roundCount;

	@Column(name = "current_player", nullable = false)
	private Integer currentPlayer;

	@Column(name = "max_player", nullable = false)
	private Integer maxPlayer;

	@Column(name = "is_started", nullable = false)
	private Boolean isStarted;

	@OneToMany(mappedBy = "gameRoom", fetch = FetchType.EAGER)
	@JsonBackReference
	List<Member> members = new ArrayList<>();

	@Builder
	public GameRoom(
		Long ownerId,
		String title,
		String password,
		String inviteCode,
		Integer roundCount,
		Integer currentPlayer,
		Integer maxPlayer,
		Boolean isStarted) {
		this.ownerId = ownerId;
		this.title = title;
		this.password = password;
		this.inviteCode = inviteCode;
		this.roundCount = roundCount;
		this.currentPlayer = currentPlayer;
		this.maxPlayer = maxPlayer;
		this.isStarted = isStarted;
	}

	public void enterGameRoom() {
		this.currentPlayer++;
	}

	public void exitGameRoom() {
		this.currentPlayer--;
	}
}
