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
 * @className: AppAddRequest
 * @date: 2025/9/19 10:52
 * @description: 新增应用参数
 */
@Schema(name = "新增应用", description = "新增应用参数")
@Data
public class AppAddRequest implements Serializable {

    /**
     * 应用初始化的 prompt
     */
    @Schema(description = "提示词")
    private String initPrompt;

    @Serial
    private static final long serialVersionUID = 1L;
}