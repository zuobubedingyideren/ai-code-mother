package com.px.aicodemother.ai.model.message;

import lombok.Getter;

/**
 * packageName: com.px.aicodemother.ai.model.message
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: StreamMessageTypeEnum
 * @date: 2025/9/23 10:38
 * @description: 流式消息类型枚举
 */
@Getter
public enum StreamMessageTypeEnum {
    AI_RESPONSE("ai_response", "AI响应"),
    TOOL_REQUEST("tool_request", "工具请求"),
    TOOL_EXECUTED("tool_executed", "工具执行结果");

    private final String value;
    private final String text;

    StreamMessageTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据value获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static StreamMessageTypeEnum getEnumByValue(String value) {
        for (StreamMessageTypeEnum typeEnum : values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
