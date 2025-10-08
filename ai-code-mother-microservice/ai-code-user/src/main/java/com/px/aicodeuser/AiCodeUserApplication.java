package com.px.aicodeuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * packageName: com.px.aicodeuser
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiCodeUserApplication
 * @date: 2025/10/8 16:10
 * @description: 用户服务启动类
 */
@SpringBootApplication
@MapperScan("com.px.aicodeuser.mapper")
@ComponentScan("com.px")
public class AiCodeUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeUserApplication.class, args);
    }
}
