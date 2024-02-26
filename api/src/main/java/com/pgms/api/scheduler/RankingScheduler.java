package com.pgms.api.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.repository.GameRankRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {
	private static final String ZONE = "Asia/Seoul";
	private static final int SCHEDULE_UPDATE_CYCLE = 10000;
	private static final int SCHEDULE_INITIAL_DELAY = 3000;

	private final GameRankRepository gameRankRepository;
	private final JdbcTemplate jdbcTemplate;

	@Scheduled(fixedRate = SCHEDULE_UPDATE_CYCLE, initialDelay = SCHEDULE_INITIAL_DELAY, zone = ZONE)
	public void run() {
		log.info(">>>>>> Ranking Scheduled Run !!!!!!!!!");
		gameRankRepository.deleteAllInBatch();

		// 게임 타입 별 랭킹 계산
		String modeRankingQuery =
			"INSERT INTO game_rank (member_id, nickname, total_score, average_wpm, average_accuracy, game_type) "
				+ "SELECT gh.member_id, m.nickname, "
				+ "SUM(gh.score) AS total_score, "
				+ "AVG(gh.wpm) AS average_wpm, "
				+ "AVG(gh.accuracy) AS average_accuracy, "
				+ "gh.game_type "
				+ "FROM game_history gh LEFT JOIN member m ON gh.member_id = m.id "
				+ "GROUP BY gh.member_id, m.nickname, gh.game_type "
				+ "ORDER BY total_score DESC LIMIT 100";
		jdbcTemplate.update(modeRankingQuery);

		// 전체 랭킹 계산
		String totalRankingQuery =
			"INSERT INTO game_rank (member_id, nickname, total_score, average_wpm, average_accuracy) "
				+ "SELECT gh.member_id, m.nickname, "
				+ "SUM(gh.score) AS total_score, "
				+ "AVG(gh.wpm) AS average_wpm, "
				+ "AVG(gh.accuracy) AS average_accuracy "
				+ "FROM game_history gh LEFT JOIN member m ON gh.member_id = m.id "
				+ "GROUP BY gh.member_id, m.nickname "
				+ "ORDER BY total_score DESC LIMIT 100";

		jdbcTemplate.update(totalRankingQuery);
	}
}
