package com.pgms.coreinfraredis.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisKeyRepository {

	private static final int REFRESH_TOKEN_TIME_OUT_DAYS = 7;
	private static final int BLACKLIST_TIME_OUT_MINUTES = 30;

	private final RedisTemplate<String, Object> redisTemplate;

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public void saveRefreshToken(String key, Object value) {
		redisTemplate.opsForValue().set(key, value, REFRESH_TOKEN_TIME_OUT_DAYS, TimeUnit.DAYS);
	}

	public void saveBlackList(String key, Object value) {
		redisTemplate.opsForValue().set(key, value, BLACKLIST_TIME_OUT_MINUTES, TimeUnit.MINUTES);
	}

	public void saveInviteCode(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public Long guestIdIncrement(String key) {
		return redisTemplate.opsForValue().increment(key);
	}
}
