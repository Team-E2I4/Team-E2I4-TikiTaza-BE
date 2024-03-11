package com.pgms.api.domain.rank.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coreinfraredis.dto.RankingResponse;
import com.pgms.coreinfraredis.repository.RedisRankingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

	private static final String REDIS_RANKING_KEY = "ranking";
	private final RedisRankingRepository redisRankingRepository;

	public List<RankingResponse> getRanking(String gameType) {
		return gameType == null ?
			redisRankingRepository.getTopRankings(REDIS_RANKING_KEY) :
			redisRankingRepository.getTopRankings(GameType.of(gameType) + REDIS_RANKING_KEY);
	}
}
