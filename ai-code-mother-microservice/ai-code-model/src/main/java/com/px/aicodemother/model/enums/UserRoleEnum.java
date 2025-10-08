package com.px.aicodemother.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * packageName: com.px.aicodemother.model.enums
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: UserRoleEnum
 * @date: 2025/9/17 22:46
 * @description: 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin");

    /**
     * 描述
     */
    private final String text;

    /**
     * 值
     */
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据值获取对应的枚举实例
     *
     * @param value 枚举值
     * @return 对应的枚举实例，如果未找到则返回null
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.value.equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
