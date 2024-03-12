package com.prgrms.zzalmyu.core.config;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // RedisProperties로 yaml에 저장한 host, post를 연결
    @Bean
    public RedisConnectionFactory defaultRedisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(defaultRedisConnectionFactory());

        return redisTemplate;
    }
    //이미지 저장용
    @Bean
    public RedisTemplate<String, ImageResponseDto> imageRedisTemplate() {
        RedisTemplate<String, ImageResponseDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(ImageResponseDto.class));
        redisTemplate.setConnectionFactory(defaultRedisConnectionFactory());

        return redisTemplate;
    }

    @Bean
    public HashOperations<String, String, String> hashOperations() {
        return redisTemplate().opsForHash();
    }
}
