package com.pgms.coreinfraredis.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pgms.coreinfraredis.dto.RankingResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRankingRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public void addScore(String key, String nickname, double score) {
		redisTemplate.opsForZSet().add(key, nickname, score);
	}

	public List<RankingResponse> getTopRankings(String key) {
		Set<Object> nicknames = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
		List<RankingResponse> rankingResponses = new ArrayList<>();

		for (Object obj : nicknames) {
			String nickname = (String)obj;
			Long rank = getMemberRank(key, nickname);
			Double score = getMemberScore(key, nickname);
			rankingResponses.add(new RankingResponse(nickname, rank, score));
		}

		return rankingResponses;
	}

	public RankingResponse getRanking(String key, String nickname) {
		final Long memberRank = getMemberRank(key, nickname);
		final Double memberScore = getMemberScore(key, nickname);
		return new RankingResponse(nickname, memberRank, memberScore);
	}

	private Long getMemberRank(String key, String nickname) {
		return redisTemplate.opsForZSet().reverseRank(key, nickname);
	}

	private Double getMemberScore(String key, String nickname) {
		return redisTemplate.opsForZSet().score(key, nickname);
	}
}
