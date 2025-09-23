package com.px.aicodemother.ai.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * packageName: com.px.aicodemother.ai.model.message
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiResponseMessage
 * @date: 2025/9/23 10:36
 * @description:  AI 响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AiResponseMessage extends StreamMessage{
    private String data;

    public AiResponseMessage(String data) {
        super(StreamMessageTypeEnum.AI_RESPONSE.getValue());
        this.data = data;
    }
}
