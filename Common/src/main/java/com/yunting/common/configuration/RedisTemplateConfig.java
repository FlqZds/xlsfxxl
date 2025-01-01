package com.yunting.common.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

@SpringBootConfiguration
public class RedisTemplateConfig {


    //    配置缓存工厂/缓存管理器
    @Bean
    public CacheManager cacheManager(@Autowired LettuceConnectionFactory ltfc) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))// 设置缓存有效期为10分钟
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // 设置value序列化方案为json
                .disableCachingNullValues();

//cacheManager只是接口，具体实现由RedisCacheManager实现
        return RedisCacheManager.builder(ltfc).cacheDefaults(config).build();
    }


    @Resource(name = "sessionInfoFactory")
    private LettuceConnectionFactory ltfc_session;

    @Resource(name = "wlanConnectionFactory")
    private LettuceConnectionFactory ltfc_wlan;

    @Resource(name = "recordInfoFactory")
    private LettuceConnectionFactory ltfc_record;

    @Bean("record_StringTemplate")
    public StringRedisTemplate recordStringTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(ltfc_record);  // 设置连接工厂

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        return redisTemplate;
    }

    @Bean("wlan_StringTemplate")
    public StringRedisTemplate wlanStringTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(ltfc_wlan);  // 设置连接工厂

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        return redisTemplate;
    }

    @Bean("session_StringTemplate")
    public StringRedisTemplate sessionStringTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(ltfc_session);  // 设置连接工厂

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        return redisTemplate;
    }

}
