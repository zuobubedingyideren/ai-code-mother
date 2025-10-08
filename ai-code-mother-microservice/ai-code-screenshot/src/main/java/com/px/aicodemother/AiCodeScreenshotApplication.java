package com.px.aicodemother;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * packageName: com.px.aicodemother
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiCodeScreenshotApplication
 * @date: 2025/10/8 17:13
 * @description: 截图服务启动类
 */
@SpringBootApplication
@EnableDubbo
public class AiCodeScreenshotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeScreenshotApplication.class, args);
    }
}
