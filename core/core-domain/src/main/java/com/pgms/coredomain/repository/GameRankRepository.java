package com.pgms.coredomain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import com.pgms.coredomain.domain.game.GameRank;
import com.pgms.coredomain.domain.game.GameType;

public interface GameRankRepository extends JpaRepository<GameRank, Long> {
	@Query("SELECT gr FROM GameRank gr WHERE (:gameType IS NULL AND gr.gameType IS NULL) OR gr.gameType = :gameType ORDER BY gr.totalScore DESC, gr.averageWpm")
	List<GameRank> findAllRanking(@Nullable GameType gameType);
}
