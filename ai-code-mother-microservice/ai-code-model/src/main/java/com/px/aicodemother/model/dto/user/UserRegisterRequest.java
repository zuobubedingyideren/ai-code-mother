package com.px.aicodemother.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * packageName: com.px.aicodemother.model.dto.user
 *
 * @author: idpeng
 * @version: 1.0
 * @className: UserRegisterRequest
 * @date: 2025/9/17 22:49
 * @description: 用户注册请求体
 */
@Data
@Schema(description = "用户注册请求体")
public class UserRegisterRequest implements Serializable {

    /**
     * 用户账号
     */
    @Schema(description = "用户账号")
    private String userAccount;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    private String userPassword;

    /**
     * 校验密码
     */
    @Schema(description = "校验密码")
    private String checkPassword;

    @Serial
    private static final long serialVersionUID = 1L;
}