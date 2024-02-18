package com.pgms.coredomain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.game.GameQuestion;
import com.pgms.coredomain.domain.game.GameType;

public interface GameQuestionRepository extends JpaRepository<GameQuestion, Long> {
	@Query("SELECT gq FROM GameQuestion gq WHERE gq.gameType = :gameType ORDER BY RAND()")
	List<GameQuestion> findByGameTypeAndCount(@Param("gameType") GameType gameType, Pageable pageable);
	//findByGameTypeAndCount(gameType, PageRequest.of(0, size));
}
