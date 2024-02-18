package com.pgms.coredomain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameInfo;

public interface GameInfoRepository extends JpaRepository<GameInfo, Long> {
	Optional<GameInfo> findByGameRoomId(Long gameRoomId);
}
