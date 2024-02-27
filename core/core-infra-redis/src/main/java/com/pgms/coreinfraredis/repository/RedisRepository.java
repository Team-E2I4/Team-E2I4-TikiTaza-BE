package com.pgms.coreinfraredis.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRepository {

	private static final int REFRESH_TOKEN_TIME_OUT_DAYS = 7;
	private static final int BLACKLIST_TIME_OUT_MINUTES = 30;
	private static final String ROUND_PREFIX = "round:";
	private static final String TOTAL_PREFIX = "total:";
	private static final String WORD_PREFIX = "word:";

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 리프레시 토큰용
	public void saveRefreshToken(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, REFRESH_TOKEN_TIME_OUT_DAYS, TimeUnit.DAYS);
	}

	public void saveInviteCode(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
	}

	// 블랙리스트용 (로그아웃)
	public void saveBlackList(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisBlackListTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, BLACKLIST_TIME_OUT_MINUTES, TimeUnit.MINUTES);
	}

	public boolean hasKeyBlackList(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 라운드별 점수 초기화
	public void initRoundScores(String roomId, List<Long> memberIds) {
		for (Long memberId : memberIds) {
			redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId.toString(), 0);
		}
	}

	// 단어게임 단어 리스트 초기화
	public void initWords(String roomId, List<String> words) {
		for (String word : words) {
			redisTemplate.opsForZSet().add(WORD_PREFIX + roomId, word, 1);
		}
	}

	// 라운드별 멤버 점수 업데이트
	public void increaseRoundScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, score);
	}

	// 단어게임 멤버 점수 업데이트
	public void increaseRoundWordScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().incrementScore(ROUND_PREFIX + roomId, memberId, score);
	}

	// 누적 멤버 점수 업데이트
	public void increaseTotalScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().incrementScore(TOTAL_PREFIX + roomId, memberId, score);
	}

	// 단어 사용 여부 업데이트 & 점수 반환
	public Long updateWords(String roomId, String word) {
		// 단어 리스트 가져옴
		Double score = redisTemplate.opsForZSet().score(WORD_PREFIX + roomId, word);
		// 단어 있는지 확인 -> 있으면 삭제 & 점수 반환
		if (score != null && score != 0.0) {
			redisTemplate.opsForZSet().remove(WORD_PREFIX + roomId, word);
			return score.longValue();
		}
		return 0L;
	}

	// 멤버 점수 조회
	public Map<Long, Long> getRoundScores(String roomId) {
		Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScoreWithScores(ROUND_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return convertToMap(membersWithScores);
	}

	// 누적 멤버 점수 조회
	public Map<Long, Long> getTotalScores(String roomId) {
		Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScoreWithScores(TOTAL_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return convertToMap(membersWithScores);
	}

	public void deleteGameInfo(String roomId) {
		redisTemplate.delete(TOTAL_PREFIX + roomId);
		redisTemplate.delete(ROUND_PREFIX + roomId);
		redisTemplate.delete(WORD_PREFIX + roomId);
	}

	// guest Id 자동 증가
	public Long guestIdIncrement(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	private Map<Long, Long> convertToMap(Set<ZSetOperations.TypedTuple<Object>> tupleSet) {
		return tupleSet.stream()
			.collect(Collectors.toMap(
				tuple -> Long.valueOf(tuple.getValue().toString()), // Integer를 Long으로 변환
				tuple -> tuple.getScore().longValue() // Double을 Long으로 변환
			));
	}
}
