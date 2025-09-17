package com.px.aicodemother.exception;

import lombok.Getter;

/**
 * packageName: com.px.aicodemother.exception
 *
 * @author: idpeng
 * @version: 1.0
 * @className: BusinessException
 * @date: 2025/9/17 16:46
 * @description: 自定义异常
 */
@Getter
public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误码 + 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 错误码枚举 + 错误信息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
