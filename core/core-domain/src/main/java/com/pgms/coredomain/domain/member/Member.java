package com.pgms.coredomain.domain.member;

import static com.pgms.coredomain.domain.member.AccountStatus.*;

import com.pgms.coredomain.domain.common.BaseEntity;
import com.pgms.coredomain.domain.game.GameRoom;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private AccountStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider")
	private ProviderType providerType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private GameRoom gameRoom;

	@Builder
	public Member(String email, String password, String nickname, ProviderType providerType, Role role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
		this.status = ACTIVE;
		this.providerType = providerType;
	}

	public boolean isDeleted() {
		return this.status == DELETED;
	}

	public void delete() {
		this.status = DELETED;
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
}
