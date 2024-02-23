package com.pgms.coredomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameHistory;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
}
