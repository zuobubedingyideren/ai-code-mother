package com.px.aicodemother.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * packageName: com.px.aicodemother.config
 *
 * @author: idpeng
 * @version: 1.0
 * @className: RedisCacheManagerConfig
 * @date: 2025/10/3 16:57
 * @description: Redis缓存管理器配置
 */
@Configuration
public class RedisCacheManagerConfig {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置并创建Redis缓存管理器
     * 
     * @return 返回配置好的Redis缓存管理器实例
     */
    @Bean
    public CacheManager cacheManager() {
        // 配置ObjectMapper以支持Java 8时间类型序列化
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 配置默认缓存设置：30分钟过期时间，不缓存null值，使用字符串序列化器
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()));
                // value 使用 JSON 序列化器（支持复杂对象）但是要注意开启后需要给序列化增加默认类型配置，否则无法反序列化
                //.serializeValuesWith(RedisSerializationContext.SerializationPair
                //.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
        // 构建缓存管理器，并为特定缓存"good_app_page"设置5分钟过期时间
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("good_app_page", defaultConfig.entryTtl(Duration.ofMinutes(5)))
                .build();
    }
}