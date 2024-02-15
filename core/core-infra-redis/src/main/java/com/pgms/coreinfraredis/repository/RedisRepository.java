package com.pgms.coreinfraredis.repository;

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
	private static final int IN_GAME_INFO_TIME_OUT_MINUTES = 30;

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	public void saveRefreshToken(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, REFRESH_TOKEN_TIME_OUT_DAYS, TimeUnit.DAYS);
	}

	public void saveInGameInfo(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, IN_GAME_INFO_TIME_OUT_MINUTES, TimeUnit.MINUTES);
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

	public void saveBlackList(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisBlackListTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, BLACKLIST_TIME_OUT_MINUTES, TimeUnit.MINUTES);
	}

	public boolean hasKeyBlackList(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
