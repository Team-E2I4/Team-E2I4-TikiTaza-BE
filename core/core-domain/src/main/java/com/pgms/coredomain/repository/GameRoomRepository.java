package com.pgms.coredomain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import com.pgms.coredomain.domain.game.GameRoom;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
	Optional<GameRoom> findById(Long gameRoomId);
}
