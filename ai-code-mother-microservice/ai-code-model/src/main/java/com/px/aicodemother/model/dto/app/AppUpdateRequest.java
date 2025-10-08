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
 * @className: AppUpdateRequest
 * @date: 2025/9/19 11:05
 * @description: 更新应用参数
 */
@Data
@Schema(name = "更新应用", description = "更新应用参数")
public class AppUpdateRequest implements Serializable {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private Long id;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String appName;

    @Serial
    private static final long serialVersionUID = 1L;
}
