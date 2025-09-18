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
 * @className: UserLoginRequest
 * @date: 2025/9/17 23:37
 * @description: 用户登录请求体
 */
@Data
@Schema(description = "用户登录请求体")
public class UserLoginRequest implements Serializable {
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

    @Serial
    private static final long serialVersionUID = 1L;
}
