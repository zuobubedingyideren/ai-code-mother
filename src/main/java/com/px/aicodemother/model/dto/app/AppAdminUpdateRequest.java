package com.px.aicodemother.model.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * packageName: com.px.aicodemother.model.dto.app
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AppAdminUpdateRequest
 * @date: 2025/9/19 14:29
 * @description: 更新管理员应用请求参数
 */
@Data
@Schema(name = "AppAdminUpdateRequest", description = "应用更新请求参数")
public class AppAdminUpdateRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "应用ID")
    private Long id;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 应用封面
     */
    @Schema(description = "应用封面")
    private String cover;

    /**
     * 优先级
     */
    @Schema(description = "优先级")
    private Integer priority;

    @Serial
    private static final long serialVersionUID = 1L;
}
