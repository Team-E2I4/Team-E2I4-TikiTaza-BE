package com.pgms.coredomain.domain.game;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GameRoomMember {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(nullable = false)
	private Long memberId;            // 유저 ID

	@Column(nullable = false)
	private String nickname;        // 유저 닉네임

	@Column
	private String webSessionId;    // webSessionId

	@Column(nullable = false)
	private boolean readyStatus;    // 레디상태

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private GameRoom gameRoom;      // 게임방

	@Builder
	public GameRoomMember(GameRoom gameRoom, Long memberId, String nickname, String webSessionId, boolean readyStatus) {
		this.gameRoom = gameRoom;
		this.memberId = memberId;
		this.nickname = nickname;
		this.webSessionId = webSessionId;
		this.readyStatus = readyStatus;
	}

	public void update(boolean readyStatus) {
		this.readyStatus = readyStatus;
	}

	public void update(boolean readyStatus, String webSessionId) {
		this.readyStatus = readyStatus;
		this.webSessionId = webSessionId;
	}
}
