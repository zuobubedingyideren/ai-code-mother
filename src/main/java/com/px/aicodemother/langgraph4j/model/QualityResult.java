package com.px.aicodemother.langgraph4j.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.model
 *
 * @author: idpeng
 * @version: 1.0
 * @className: QualityResult
 * @date: 2025/9/29 19:59
 * @description: 代码质量检查结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否通过质检
     */
    private Boolean isValid;

    /**
     * 错误列表
     */
    private List<String> errors;

    /**
     * 改进建议
     */
    private List<String> suggestions;
}
