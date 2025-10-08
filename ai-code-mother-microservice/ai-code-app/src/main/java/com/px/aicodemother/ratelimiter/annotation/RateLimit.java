package com.px.aicodemother.ratelimiter.annotation;

import com.px.aicodemother.ratelimiter.enums.RateLimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName: com.px.aicodemother.ratelimiter.annotation
 *
 * @author: idpeng
 * @version: 1.0
 * @annotationTypeName: RateLimit
 * @date: 2025/10/3 20:26
 * @description: 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流key
     * @return 限流key
     */
    String key() default "";

    /**
     * 每一个时间窗口允许的请求数
     * @return 限流次数
     */
    int rate() default 10;

    /**
     * 时间窗口，单位秒
     * @return 时间窗口
     */
    int rateInterval() default 1;

    /**
     * 限流类型
     * @return 限流类型
     */
    RateLimitType rateLimitType() default RateLimitType.USER;

    /**
     * 错误信息
     * @return 错误信息
     */
    String message() default "请求过于频繁，请稍后再试";
}
