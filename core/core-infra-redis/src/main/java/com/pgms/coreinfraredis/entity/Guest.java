package com.pgms.coreinfraredis.entity;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "guest", timeToLive = 36000L)
public class Guest implements Serializable {
	@Id
	private Long id;

	private String nickname;
}
