package com.px.aicodemother.common;

import com.px.aicodemother.exception.ErrorCode;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * packageName: com.px.aicodemother.common
 *
 * @author: idpeng
 * @version: 1.0
 * @className: BaseResponse
 * @date: 2025/9/17 16:52
 * @description: 封装通用响应结果类
 */
@Data
@Schema(description = "通用响应结果类")
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private int code;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private T data;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String message;

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param data    数据
     * @param message 错误信息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param data    数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}