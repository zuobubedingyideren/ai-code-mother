package com.px.aicodemother;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * packageName: com.px.aicodemother
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiCodeAppApplication
 * @date: 2025/10/8 16:56
 * @description: ai应用启动类
 */
@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.px.aicodemother.mapper")
@EnableCaching
public class AiCodeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeAppApplication.class, args);
    }
}
