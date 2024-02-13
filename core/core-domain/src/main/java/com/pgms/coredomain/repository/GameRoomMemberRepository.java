package com.pgms.coredomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameRoomMember;

public interface GameRoomMemberRepository extends JpaRepository<GameRoomMember, Long> {
	boolean existsByMemberId(Long memberId);

	void deleteByMemberId(Long memberId);
}
