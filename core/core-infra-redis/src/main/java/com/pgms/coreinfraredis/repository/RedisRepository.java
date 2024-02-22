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
	public void initMemberScores(String roomId, List<Long> memberIds) {
		for (Long memberId : memberIds) {
			redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, 0);
		}
	}

	// 단어게임 단어 리스트 초기화
	public void initWords(String roomId, List<String> words) {
		for (String word : words) {
			redisTemplate.opsForZSet().add(WORD_PREFIX + roomId, word, 1);
		}
	}

	// 라운드별 멤버 점수 업데이트
	public void updateRoundMemberScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().add(ROUND_PREFIX + roomId, memberId, score);
	}

	// 단어게임 멤버 점수 업데이트
	public void updateWordGameMemberScore(String roomId, String memberId, Long score) {
		redisTemplate.opsForZSet().incrementScore(WORD_PREFIX + roomId, memberId, score);
	}

	// 단어 맵 업데이트
	public Long updateWords(String roomId, String word) {
		// 단어 리스트 가져옴
		Long score = getWords(roomId).getOrDefault(word, 0L);
		// 단어 있는지 확인 -> 있으면 삭제 & score 리턴
		if (score != 0L) {
			redisTemplate.opsForZSet().remove(WORD_PREFIX + roomId, word);
		}
		return score;
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

	public Map<String, Long> getWords(String roomId) {
		Set<Object> wordsWithScores = redisTemplate.opsForZSet()
			.rangeByScore(WORD_PREFIX + roomId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return convertToMap(roomId, wordsWithScores);
	}

	private <K> Map<K, Long> convertToMap(String roomId, Set<Object> scores) {
		Map<K, Long> result = new HashMap<>();
		for (Object score : Objects.requireNonNull(scores)) {
			K key = (K)score; //member or word
			Double findScore = redisTemplate.opsForZSet().score(roomId, key);
			if (findScore != null) {
				result.put(key, findScore.longValue());
			}
		}
		return result;
	}

	public void deleteGameScores(String roomId) {
		redisTemplate.delete(TOTAL_PREFIX + roomId);
		redisTemplate.delete(ROUND_PREFIX + roomId);
	}

	public void deleteWords(String roomId) {
		redisTemplate.delete(WORD_PREFIX + roomId);
	}
}
