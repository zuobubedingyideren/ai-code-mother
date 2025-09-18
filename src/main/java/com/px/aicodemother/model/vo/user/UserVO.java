package com.px.aicodemother.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * packageName: com.px.aicodemother.model.vo.user
 *
 * @author: idpeng
 * @version: 1.0
 * @className: UserVO
 * @date: 2025/9/18 09:57
 * @description: 用户视图对象
 */
@Data
@Schema(description = "用户视图对象")
public class UserVO implements Serializable {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String userAccount;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

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
     * 用户角色：user/admin
     */
    @Schema(description = "用户角色：user/admin")
    private String userRole;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
