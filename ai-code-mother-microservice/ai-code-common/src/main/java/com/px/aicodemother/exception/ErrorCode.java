package com.px.aicodemother.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * packageName: com.px.aicodemother.exception
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: ErrorCode
 * @date: 2025/9/17 16:44
 * @description: 错误码枚举类
 */
@Getter
@Schema(description = "错误码枚举类")
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    TOO_MANY_REQUEST(42900, "请求过于频繁"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private final int code;

    /**
     * 状态码信息
     */
    @Schema(description = "状态码信息")
    private final String message;

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 状态码信息
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}