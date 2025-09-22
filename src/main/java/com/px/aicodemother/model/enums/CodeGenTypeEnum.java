package com.px.aicodemother.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * packageName: com.px.aicodemother.model.enums
 *
 * @author: idpeng
 * @version: 1.0
 * @enumName: CodeGenTypeEnum
 * @date: 2025/9/18 21:20
 * @description: 代码生成类型枚举
 */
@Getter
public enum CodeGenTypeEnum {
    HTML("原生 HTML 模式", "html"),
    MULTI_FILE("原生多文件模式", "multi_file"),
    VUE_PROJECT("Vue 工程模式", "vue_project");


    /**
     * 描述
     */
    private final String text;

    /**
     * 值
     */
    private final String value;

    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据值获取对应的枚举实例
     *
     * @param value 枚举值
     * @return 匹配的枚举实例，未找到则返回null
     */
    public static CodeGenTypeEnum getEnumByValue(String value) {

        if (ObjectUtil.isEmpty(value)) {
            return null;
        }

        for (CodeGenTypeEnum valueEnum : CodeGenTypeEnum.values()) {
            if (valueEnum.value.equals(value)) {
                return valueEnum;
            }
        }
        return null;
    }
}
