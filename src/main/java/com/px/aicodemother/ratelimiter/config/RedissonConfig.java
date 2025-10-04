package com.px.aicodemother.ratelimiter.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.EqualJitterDelay;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName: com.px.aicodemother.ratelimiter.config
 *
 * @author: idpeng
 * @version: 1.0
 * @className: RedissonConfig
 * @date: 2025/10/3 20:13
 * @description: Redisson配置类
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.database}")
    private Integer redisDatabase;

    /**
     * 创建并配置Redisson客户端实例
     * 
     * @return 返回配置好的Redisson客户端实例
     */
    @Bean
    public RedissonClient redissonClient() {
        // 创建Redisson配置对象并设置连接地址
        Config config = new Config();
        String address = "redis://" + redisHost + ":" + redisPort;
        
        // 配置单机Redis服务器连接参数
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(10)
                .setIdleConnectionTimeout(30000)
                .setConnectTimeout(5000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryDelay(new EqualJitterDelay(
                    java.time.Duration.ofMillis(1000), 
                    java.time.Duration.ofMillis(2000)
                ));

        // 如果配置了密码则设置密码
        if (redisPassword != null && !redisPassword.isEmpty()) {
            singleServerConfig.setPassword(redisPassword);
        }
        
        // 根据配置创建并返回Redisson客户端
        return Redisson.create(config);
    }
}