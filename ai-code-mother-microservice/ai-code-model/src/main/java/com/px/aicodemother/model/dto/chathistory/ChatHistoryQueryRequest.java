package com.px.aicodemother.model.dto.chathistory;

import com.px.aicodemother.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * packageName: com.px.aicodemother.model.dto
 *
 * @author: idpeng
 * @version: 1.0
 * @className: chathistory
 * @date: 2025/9/21 16:48
 * @description: 游标查询的请求对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "游标查询的请求对象")
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String message;

    /**
     * 消息类型（user/ai）
     */
    @Schema(description = "消息类型（user/ai）")
    private String messageType;

    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private Long appId;

    /**
     * 创建用户id
     */
    @Schema(description = "创建用户id")
    private Long userId;

    /**
     * 游标查询 - 最后一条记录的创建时间
     * 用于分页查询，获取早于此时间的记录
     */
    @Schema(description = "游标查询 - 最后一条记录的创建时间")
    private LocalDateTime lastCreateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
