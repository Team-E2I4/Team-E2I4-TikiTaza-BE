package com.pgms.coredomain.domain.game;

import com.pgms.coredomain.domain.common.BaseEntity;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "game_room_member")
public class GameRoomMember extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "web_session_id")
	private String webSessionId;

	@Column(name = "ready_status", nullable = false)
	private boolean readyStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private GameRoom gameRoom;

	@Builder
	public GameRoomMember(
		GameRoom gameRoom,
		Long memberId,
		String nickname,
		String webSessionId,
		boolean readyStatus) {
		this.gameRoom = gameRoom;
		this.memberId = memberId;
		this.nickname = nickname;
		this.webSessionId = webSessionId;
		this.readyStatus = readyStatus;
	}

	public void updateReadyStatus(boolean readyStatus) {
		this.readyStatus = readyStatus;
	}

	public void updateSessionId(String sessionId) {
		this.webSessionId = sessionId;
	}
}
