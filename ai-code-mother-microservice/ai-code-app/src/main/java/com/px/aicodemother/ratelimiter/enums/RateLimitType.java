package com.px.aicodemother.ratelimiter.enums;

/**
 * packageName: com.px.aicodemother.ratelimiter.enums
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: RateLimitType
 * @date: 2025/10/3 20:24
 * @description: 限流类型枚举类
 */
public enum RateLimitType {

    /**
     * API级别限流
     */
    API,
    /**
     * 用户级别限流
     */
    USER,
    /**
     * IP级别限流
     */
    IP
}
