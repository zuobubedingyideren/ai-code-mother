package com.px.aicodemother.aop;

import com.px.aicodemother.annotation.AuthCheck;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.innerservice.InnerUserService;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.enums.UserRoleEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * packageName: com.px.aicodemother.aop
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AuthInterceptor
 * @date: 2025/9/18 09:40
 * @description: 权限拦截器
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    @Lazy
    private InnerUserService userService;


    /**
     * 执行权限检查拦截器
     * @param joinPoint 连接点
     * @param authCheck 权限检查注解
     * @return 目标方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = InnerUserService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        // 如果不需要特定角色，则直接执行目标方法
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        // 校验用户角色权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        
        // 如果方法要求管理员权限但用户不是管理员，则抛出无权限异常
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        
        // 权限校验通过，执行目标方法
        return joinPoint.proceed();
    }
}
