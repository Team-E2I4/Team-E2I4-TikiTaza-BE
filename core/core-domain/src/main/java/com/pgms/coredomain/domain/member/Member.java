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

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Column(name = "provider")
	@Enumerated(EnumType.STRING)
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
}
