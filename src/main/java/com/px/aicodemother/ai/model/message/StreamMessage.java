package com.px.aicodemother.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName: com.px.aicodemother.ai.model.message
 *
 * @author: idpeng
 * @version: 1.0
 * @className: StreamMessage
 * @date: 2025/9/23 10:34
 * @description: 流式消息响应基类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {
    private String type;
}
