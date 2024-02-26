package com.pgms.coredomain.domain.game;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "game_info")
public class GameInfo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_room_id")
	private Long gameRoomId;

	@Column(name = "entered_member_count")
	private int enteredMemberCount;

	@Column(name = "submitted_member_count")
	private int submittedMemberCount;

	public GameInfo(Long gameRoomId) {
		this.gameRoomId = gameRoomId;
		this.enteredMemberCount = 0;
		this.submittedMemberCount = 0;
	}

	public boolean isAllSubmitted(int currentPlayer) {
		return currentPlayer == submittedMemberCount;
	}

	public boolean isAllEntered(int currentPlayer) {
		return currentPlayer == enteredMemberCount;
	}

	public void enter() {
		enteredMemberCount++;
	}

	public void submit() {
		submittedMemberCount++;
	}

	public void initSubmittedCount() {
		submittedMemberCount = 0;
	}
}
