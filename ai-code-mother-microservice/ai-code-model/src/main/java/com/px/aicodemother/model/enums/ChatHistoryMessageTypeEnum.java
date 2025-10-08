package com.px.aicodemother.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * packageName: com.px.aicodemother.model.enums
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: ChatHistoryMessageTypeEnum
 * @date: 2025/9/21 16:16
 * @description: 聊天记录消息类型枚举
 */
@Getter
public enum ChatHistoryMessageTypeEnum {

    USER("用户", "user"),
    AI("AI", "ai");

    private final String text;

    private final String value;

    ChatHistoryMessageTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static ChatHistoryMessageTypeEnum getEnumByValue(String value) {
        if (StrUtil.isEmpty(value)) {
            return null;
        }
        for (ChatHistoryMessageTypeEnum anEnum : ChatHistoryMessageTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
