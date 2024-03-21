package com.pgms.api.scheduler;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.coreinfraredis.dto.RankingRequest;
import com.pgms.coreinfraredis.repository.RedisRankingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {

	private static final String ZONE = "Asia/Seoul";
	private static final int SCHEDULE_UPDATE_CYCLE = 300000;
	private static final int SCHEDULE_INITIAL_DELAY = 3000;
	private static final String REDIS_RANKING_KEY = "ranking";

	private final RedisRankingRepository redisRankingRepository;
	private final JdbcTemplate jdbcTemplate;

	@Scheduled(fixedRate = SCHEDULE_UPDATE_CYCLE, initialDelay = SCHEDULE_INITIAL_DELAY, zone = ZONE)
	@Transactional
	public void run() {
		log.info(">>>>>> Ranking Scheduled Run !!!!!!!!!");
		// 게임 타입 별 랭킹 계산
		calculateRankingByGameType();

		// 전체 랭킹 계산
		calculateTotalRanking();
	}

	private void calculateTotalRanking() {
		String totalRankingQuery = "SELECT m.nickname AS nickname, SUM(gh.score) AS total_score "
			+ "FROM game_history gh INNER JOIN member m ON gh.member_id = m.id "
			+ "GROUP BY gh.member_id, m.nickname";

		List<RankingRequest> rankingResponses = jdbcTemplate.query(totalRankingQuery, (rs, rowNum) ->
			RankingRequest.builder()
				.nickname(rs.getString("nickname"))
				.score(rs.getDouble("total_score"))
				.build()
		);

		deleteAllRankings(REDIS_RANKING_KEY);
		rankingResponses.forEach(
			ranking -> redisRankingRepository.addScore(REDIS_RANKING_KEY, ranking.getNickname(), ranking.getScore()));
	}

	private void calculateRankingByGameType() {
		String totalRankingQuery =
			"SELECT m.nickname AS nickname, SUM(gh.score) AS total_score, gh.game_type AS game_type "
				+ "FROM game_history gh INNER JOIN member m ON gh.member_id = m.id "
				+ "GROUP BY gh.member_id, m.nickname, gh.game_type";

		List<RankingRequest> rankingResponses = jdbcTemplate.query(totalRankingQuery, (rs, rowNum) ->
			RankingRequest.builder()
				.nickname(rs.getString("nickname"))
				.score(rs.getDouble("total_score"))
				.gameType(rs.getString("game_type"))
				.build()
		);

		deleteAllRankings("CODE" + REDIS_RANKING_KEY);
		deleteAllRankings("WORD" + REDIS_RANKING_KEY);
		deleteAllRankings("SENTENCE" + REDIS_RANKING_KEY);

		if (!rankingResponses.isEmpty()) {
			rankingResponses.forEach(
				ranking -> redisRankingRepository.addScore(
					ranking.getGameType() + REDIS_RANKING_KEY,
					ranking.getNickname(),
					ranking.getScore()));
		}
	}

	private void deleteAllRankings(String key) {
		if (redisRankingRepository.hasKey(key)) {
			redisRankingRepository.delete(key);
		}
	}
}
