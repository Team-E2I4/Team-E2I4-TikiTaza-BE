package com.pgms.coredomain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameRoomMember;

public interface GameRoomMemberRepository extends JpaRepository<GameRoomMember, Long> {
	List<GameRoomMember> findAllByGameRoomId(Long roomId);

	Optional<GameRoomMember> findByMemberId(Long memberId);

	Optional<GameRoomMember> findByWebSessionId(String sessionId);
}
