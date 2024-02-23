package com.pgms.coredomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameRank;

public interface GameRankRepository extends JpaRepository<GameRank, Long> {
}
