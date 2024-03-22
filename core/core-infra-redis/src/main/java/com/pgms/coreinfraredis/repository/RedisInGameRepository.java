package com.pgms.coreinfraredis.repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisInGameRepository {

	private static final String ROUND_PREFIX = "round:";
	private static final String TOTAL_PREFIX = "total:";
	private static final String WORD_PREFIX = "word:";
	private static final String USED_WORD_PREFIX = "#";

	private final RedisTemplate<String, Object> redisTemplate;

	public void initRoundScores(String roomId, List<Long> memberIds) {
		String key = deleteAlreadyExistKey(ROUND_PREFIX, roomId);
		memberIds.forEach(memberId -> redisTemplate.opsForZSet().add(key, memberId.toString(), 0));
	}

	public void initWords(String roomId, List<String> words) {
		String key = deleteAlreadyExistKey(WORD_PREFIX, roomId);
		AtomicInteger index = new AtomicInteger(1);
		words.forEach(word -> redisTemplate.opsForZSet().add(key, word, index.getAndIncrement()));
	}

	// 라운드별 멤버 점수 업데이트
	public void increaseRoundScore(String roomId, String memberId, double score) {
		redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, score);
	}

	public void increaseWeight(String roomId, String memberId, double weight) {
		redisTemplate.opsForZSet().incrementScore(ROUND_PREFIX + roomId, memberId, weight);
	}

	// 단어게임 멤버 점수 업데이트
	public void increaseRoundWordScore(String roomId, String memberId) {
		redisTemplate.opsForZSet().incrementScore(ROUND_PREFIX + roomId, memberId, 1L);
	}

	// 누적 멤버 점수 업데이트
	public void increaseTotalScore(String roomId, String memberId, double score) {
		redisTemplate.opsForZSet().incrementScore(TOTAL_PREFIX + roomId, memberId, score);
	}

	public List<String> getWords(String roomId) {
		Set<Object> words = redisTemplate.opsForZSet().range(WORD_PREFIX + roomId, 0, -1);
		return Objects.requireNonNull(words).stream()
			.map(Object::toString)
			.toList();
	}

	public boolean updateWords(String roomId, String word) {
		Double index = redisTemplate.opsForZSet().score(WORD_PREFIX + roomId, word);
		if (index != null && !word.startsWith(USED_WORD_PREFIX)) {
			redisTemplate.opsForZSet().remove(WORD_PREFIX + roomId, word);
			redisTemplate.opsForZSet().add(WORD_PREFIX + roomId, USED_WORD_PREFIX + word, index);
			return true;
		}
		return false;
	}

	// 멤버 점수 조회
	public Double getRoundScore(String roomId, String memberId) {
		Double score = redisTemplate.opsForZSet().score(ROUND_PREFIX + roomId, memberId);
		return score != null ? score : 0.0;
	}

	public Map<Long, Double> getRoundScores(String roomId) {
		Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScoreWithScores(ROUND_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		return convertToMap(membersWithScores);
	}

	// 누적 멤버 점수 조회
	public Double getTotalScore(String roomId, String memberId) {
		Double score = redisTemplate.opsForZSet().score(TOTAL_PREFIX + roomId, memberId);
		return score != null ? score : 0.0;
	}

	public Map<Long, Double> getTotalScores(String roomId) {
		Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScoreWithScores(TOTAL_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		return convertToMap(membersWithScores);
	}

	public void deleteGameInfo(String roomId) {
		redisTemplate.delete(TOTAL_PREFIX + roomId);
		redisTemplate.delete(ROUND_PREFIX + roomId);
		redisTemplate.delete(WORD_PREFIX + roomId);
	}

	private Map<Long, Double> convertToMap(Set<ZSetOperations.TypedTuple<Object>> tupleSet) {
		return tupleSet.stream()
			.collect(Collectors.toMap(
				tuple -> Long.valueOf(tuple.getValue().toString()),
				ZSetOperations.TypedTuple::getScore
			));
	}

	private String deleteAlreadyExistKey(String prefix, String roomId) {
		String key = prefix + roomId;
		if (redisTemplate.hasKey(key)) {
			redisTemplate.delete(key);
		}
		return key;
	}
}
