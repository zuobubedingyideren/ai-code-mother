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
 * @className: AppDeployRequest
 * @date: 2025/9/20 15:31
 * @description:
 */
@Data
@Schema(description = "应用部署请求参数")
public class AppDeployRequest implements Serializable {

    /**
     * 应用 id
     */
    @Schema(description = "应用 id")
    private Long appId;

    @Serial
    private static final long serialVersionUID = 1L;
}
