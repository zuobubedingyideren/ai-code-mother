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
 * @className: UserAddRequest
 * @date: 2025/9/18 09:51
 * @description: 用户添加请求体
 */
@Data
@Schema(description = "用户添加请求体")
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String userAccount;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Schema(description = "用户简介")
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    @Schema(description = "用户角色: user, admin")
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}
