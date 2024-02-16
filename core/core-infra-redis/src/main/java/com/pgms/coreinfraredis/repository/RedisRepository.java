package com.pgms.coreinfraredis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRepository {

	private static final int REFRESH_TOKEN_TIME_OUT_DAYS = 7;
	private static final int BLACKLIST_TIME_OUT_MINUTES = 30;
	private static final String ROUND_PREFIX = "round:";
	private static final String TOTAL_PREFIX = "total:";

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	// 리프레시 토큰용
	public void saveRefreshToken(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, REFRESH_TOKEN_TIME_OUT_DAYS, TimeUnit.DAYS);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
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
	public void initMemberScores(String roomId, List<Long> memberIds) {
		for (Long memberId : memberIds) {
			redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, 0);
		}
	}

	// 라운드별 멤버 점수 업데이트
	public void updateRoundMemberScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, score);
	}

	// 누적 멤버 점수 업데이트
	public void updateTotalMemberScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().incrementScore(TOTAL_PREFIX + roomId, memberId, score);
	}

	// 게임방 멤버 점수 조회
	public Map<Long, Long> getRoundScores(String roomId) {
		Set<Object> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScore(ROUND_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return convertToMap(roomId, membersWithScores);
	}

	public Map<Long, Long> getTotalScores(String roomId) {
		Set<Object> membersWithScores = redisTemplate.opsForZSet()
			.rangeByScore(TOTAL_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return convertToMap(roomId, membersWithScores);
	}

	private Map<Long, Long> convertToMap(String roomId, Set<Object> membersWithScores) {
		Map<Long, Long> result = new HashMap<>();
		for (Object memberWithScore : Objects.requireNonNull(membersWithScores)) {
			Long member = Long.parseLong(memberWithScore.toString());
			Double score = redisTemplate.opsForZSet().score(roomId, memberWithScore);
			if (score != null) {
				result.put(member, score.longValue());
			}
		}
		return result;
	}

	public void deleteGameScores(String key) {
		redisTemplate.delete(TOTAL_PREFIX + key);
		redisTemplate.delete(ROUND_PREFIX + key);
	}
}
