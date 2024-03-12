package com.prgrms.zzalmyu.domain.chat.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@Transactional
@Slf4j
public class ChatRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ChatRedisService(@Qualifier("chatRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String data, Duration duration) {
        log.info("redis: " + redisTemplate);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return values.get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    protected boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}
