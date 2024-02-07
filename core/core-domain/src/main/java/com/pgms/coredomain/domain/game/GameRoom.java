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

	@Column(name = "invite_code", nullable = false)
	private String inviteCode;

	@Column(name = "current_member", nullable = false)
	private Integer currentMember;

	@OneToMany(mappedBy = "gameRoom", fetch = FetchType.EAGER)
	@JsonBackReference
	List<Member> members = new ArrayList<>();

	@Builder
	public GameRoom(String inviteCode, Long ownerId, Integer currentMember) {
		this.inviteCode = inviteCode;
		this.ownerId = ownerId;
		this.currentMember = currentMember;
	}

	public void enterMember() {
		this.currentMember++;
	}

	public void exitMember() {
		this.currentMember--;
	}
}
