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

	@Column(name = "total_score")
	private int totalScore;

	@Column(name = "average_wpm")
	private int averageWpm;

	@Column(name = "average_accuracy")
	private int averageAccuracy;

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

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updatePersonalRecord(int totalScore, int averageWpm, int averageAccuracy) {
		this.totalScore = totalScore;
		this.averageWpm = averageWpm;
		this.averageAccuracy = averageAccuracy;
	}
}
