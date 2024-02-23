package com.pgms.api.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.repository.GameHistoryRepository;
import com.pgms.coredomain.repository.GameRankRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingScheduler {
	private static final String ZONE = "Asia/Seoul";

	private final GameRankRepository gameRankRepository;
	private final GameHistoryRepository gameHistoryRepository;
	private final JdbcTemplate jdbcTemplate;

	// @Scheduled(cron = "* * * * * *", zone = "Asia/Seoul")
	@Scheduled(fixedRate = 60000, initialDelay = 3000, zone = ZONE)
	public void run() {
		// 게임 타입 별 랭킹 계산
		String query = "SELECT gh.member_id, m.nickname, "
			+ "SUM(gh.score) AS total_score, "
			+ "AVG(gh.wpm) AS average_wpm, "
			+ "AVG(gh.accuracy) AS average_accuracy "
			+ "FROM game_history gh JOIN member m ON gh.member_id = m.id"
			+ "GROUP BY gh.member_id, m.nickname"
			+ "ORDER BY total_score DESC LIMIT 100";
		// 전체 랭킹 계산

	}
}
