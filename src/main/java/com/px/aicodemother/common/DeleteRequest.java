package com.px.aicodemother.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * packageName: com.px.aicodemother.common
 *
 * @author: idpeng
 * @version: 1.0
 * @className: DeleteRequest
 * @date: 2025/9/17 17:04
 * @description: 删除请求
 */
@Data
@Schema(description = "删除请求参数")
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}
