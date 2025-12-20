package com.minisearch;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTest implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    public RedisTest(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    // Prokect has @NullMarked as default (params might be null), but CommandLineRunner has no null annotations, so have to mention here that args is non null
    public void run(String @NonNull... args) throws Exception {
        redisTemplate.opsForValue().set("ping", "pong");
        String value = redisTemplate.opsForValue().get("ping");
        System.out.println("Redis test: ping -> " + value);
    }
}
