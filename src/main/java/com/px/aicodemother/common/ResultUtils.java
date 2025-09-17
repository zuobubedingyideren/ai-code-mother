package com.px.aicodemother.common;

import com.px.aicodemother.exception.ErrorCode;

/**
 * packageName: com.px.aicodemother.common
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ResultUtils
 * @date: 2025/9/17 16:55
 * @description: 响应工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data)
    {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode)
    {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 响应
     */
    public static <T> BaseResponse<T> error(int code, String message)
    {
        return new BaseResponse<>(code,null, message);
    }

    /**
     * 错误
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @return 响应
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message)
    {
        return new BaseResponse<>(errorCode.getCode(),null, message);
    }
}
