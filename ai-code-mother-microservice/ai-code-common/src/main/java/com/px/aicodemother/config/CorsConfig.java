package com.px.aicodemother.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * packageName: com.px.aicodemother.config
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CorsConfig
 * @date: 2025/9/17 17:11
 * @description: 跨域配置类
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {


    /**
     * 配置跨域请求映射规则
     *
     * @param registry CORS注册器，用于添加跨域映射规则
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**")
               .allowCredentials(true)
               .allowedOriginPatterns("*")
               .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
               .allowedHeaders("*")
               .exposedHeaders("*");
    }
}
