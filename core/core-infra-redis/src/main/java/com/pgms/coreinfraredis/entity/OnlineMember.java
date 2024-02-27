package com.pgms.coreinfraredis.entity;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "onlineMember", timeToLive = 20L)
public class OnlineMember {
	@Id
	private Long id;

	private String nickname;
}
