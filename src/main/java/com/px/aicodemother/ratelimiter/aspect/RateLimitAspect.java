package com.px.aicodemother.ratelimiter.aspect;

import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.ratelimiter.annotation.RateLimit;
import com.px.aicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * packageName: com.px.aicodemother.ratelimiter.aspect
 *
 * @author: idpeng
 * @version: 1.0
 * @className: RateLimitAspect
 * @date: 2025/10/3 20:31
 * @description:  限流切面类
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserService userService;

    /**
     * 前置通知方法，在被@RateLimit注解标记的方法执行前进行限流检查
     * 
     * 该方法会根据限流注解配置创建或获取Redisson分布式限流器，
     * 并尝试获取执行许可。如果超出限流配置则抛出业务异常。
     * 
     * @param point 切点信息，包含被拦截的方法信息
     * @param rateLimit 限流注解，包含具体的限流配置参数
     */
    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint point, RateLimit rateLimit) {
        String key = generateRateLimitKey(point, rateLimit);
        // 获取Redisson分布式限流器实例
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        // 设置限流器1小时后过期，避免Redis中存储过多无用数据
        rateLimiter.expire(Duration.ofHours(1));
        // 设置限流器参数：每个时间窗口允许的请求数和时间窗口
        rateLimiter.trySetRate(RateType.OVERALL, rateLimit.rate(), rateLimit.rateInterval(), RateIntervalUnit.SECONDS);
        // 尝试获取令牌，如果获取失败则抛出异常
        if (!rateLimiter.tryAcquire(1)) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, rateLimit.message());
        }
    }

    /**
     * 生成限流器的唯一键值
     * 
     * 根据限流类型和相关参数生成Redis中限流器的唯一键值，用于区分不同的限流策略
     * 
     * @param point 切点信息，包含被拦截的方法信息
     * @param rateLimit 限流注解，包含限流配置信息
     * @return 限流器在Redis中的唯一键值
     */
    private String generateRateLimitKey(JoinPoint point, RateLimit rateLimit) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("rate_limit:");

        // 如果注解中指定了key，则添加到键值中
        if (!rateLimit.key().isEmpty()) {
            keyBuilder.append(rateLimit.key()).append(":");
        }
        
        // 根据不同的限流类型构建键值
        switch (rateLimit.rateLimitType()) {
            case API -> {
                // API级别限流，基于类名和方法名构建键值
                MethodSignature signature = (MethodSignature) point.getSignature();
                Method method = signature.getMethod();
                keyBuilder.append("api").append(method.getDeclaringClass().getSimpleName()).append(".").append(method.getName());
                break;
            }
            case USER -> {
                // 用户级别限流，尝试获取当前登录用户ID，如果获取失败则使用IP地址
                try {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest request = attributes.getRequest();
                        User loginUser = userService.getLoginUser(request);
                        keyBuilder.append("user:").append(loginUser.getId());
                    } else {
                        // 如果无法获取请求上下文，则使用IP地址
                        keyBuilder.append("ip:").append(getClientIP());
                    }
                } catch (Exception e) {
                    // 出现异常时回退到使用IP地址进行限流
                    keyBuilder.append("ip:").append(getClientIP());
                }
                break;
            }
            case IP -> {
                // IP级别限流，直接使用客户端IP地址构建键值
                keyBuilder.append("ip:").append(getClientIP());
                break;
            }
            default -> {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的限流类型");
            }
        }
        return keyBuilder.toString();
    }

    /**
     * 获取客户端IP地址
     * 
     * 该方法会尝试从HTTP请求中获取客户端的真实IP地址，考虑了多种情况：
     * 1. 优先从"x-forwarded-for"请求头获取（处理经过代理的情况）
     * 2. 如果获取不到，则从"X-Real-IP"请求头获取
     * 3. 如果 still 获取不到，则从请求的远程地址获取
     * 4. 如果IP地址包含多个（用逗号分隔），则取第一个
     *
     * @return 客户端IP地址，如果无法获取则返回"unknown"
     */
    private String getClientIP() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        HttpServletRequest request = attributes.getRequest();
        // 优先从x-forwarded-for请求头获取IP
        String ip = request.getHeader("x-forwarded-for");
        // 如果x-forwarded-for中没有获取到，则尝试从X-Real-IP获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        // 如果前面都没有获取到，则使用请求的远程地址
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果IP地址包含多个（用逗号分隔），则取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip != null ? ip : "unknown";
    }
}
