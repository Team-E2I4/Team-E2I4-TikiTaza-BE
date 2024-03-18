package com.pgms.coredomain.domain.member;

import static com.pgms.coredomain.domain.member.AccountStatus.*;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "game_count", nullable = false)
	private int gameCount;

	@Column(name = "round_count", nullable = false)
	private int roundCount;

	@Column(name = "average_cpm", nullable = false)
	private double averageCpm;

	@Column(name = "average_accuracy", nullable = false)
	private double averageAccuracy;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private AccountStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider")
	private ProviderType providerType;

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

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void increaseGameCount() {
		this.gameCount++;
	}

	public void updateMemberStats(double cpm, double accuracy) {
		roundCount++;
		this.averageCpm = (this.averageCpm * (roundCount - 1) + cpm) / roundCount;
		this.averageAccuracy = (this.averageAccuracy * (roundCount - 1) + accuracy) / roundCount;
	}
}
