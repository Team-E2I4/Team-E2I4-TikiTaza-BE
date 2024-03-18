package com.pgms.coredomain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import com.pgms.coredomain.domain.game.GameInfo;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface GameInfoRepository extends JpaRepository<GameInfo, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
	Optional<GameInfo> findByGameRoomId(Long gameRoomId);

	void deleteByGameRoomId(Long gameRoomId);
}
