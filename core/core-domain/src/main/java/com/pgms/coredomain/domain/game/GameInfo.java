package com.pgms.coredomain.domain.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GameInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_room_id")
	private Long gameRoomId;

	@Column(name = "entered_member_count")
	private int enteredMemberCount;

	@Column(name = "submitted_member_count")
	private int submittedMemberCount;

	public boolean isAllSubmitted(int currentPlayer) {
		return currentPlayer == submittedMemberCount;
	}

	public boolean isAllEntered(int currentPlayer) {
		return currentPlayer == submittedMemberCount;
	}

	public void enter() {
		enteredMemberCount++;
	}

	public void submit() {
		submittedMemberCount++;
	}
}