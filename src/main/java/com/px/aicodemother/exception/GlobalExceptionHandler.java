package com.px.aicodemother.exception;

import com.px.aicodemother.common.BaseResponse;
import com.px.aicodemother.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName: com.px.aicodemother.exception
 *
 * @author: idpeng
 * @version: 1.0
 * @className: GlobalExceptionHandler
 * @date: 2025/9/17 16:59
 * @description: 全局异常处理器
 */
@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 统一处理所有业务异常
     *
     * @param e 业务异常
     * @return 响应
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: ", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 统一处理所有运行时异常
     *
     * @param e 运行时异常
     * @return 响应
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
