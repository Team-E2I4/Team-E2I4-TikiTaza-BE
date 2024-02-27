package com.pgms.coreinfraredis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories("com.pgms.coreinfraredis")
public class CoreRedisConfig {
}
