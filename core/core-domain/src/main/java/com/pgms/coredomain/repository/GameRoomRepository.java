package com.pgms.coredomain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameRoom;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long>, GameRoomQuerydslRepository {

	Optional<GameRoom> findByOwnerId(Long memberId);
}
