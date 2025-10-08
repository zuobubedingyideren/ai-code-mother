package com.px.aicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * packageName: com.px.aicodemother.ai.model
 *
 * @author: idpeng
 * @version: 1.0
 * @className: MultiFileCodeResult
 * @date: 2025/9/18 21:10
 * @description: 多文件代码结果
 */
@Description("多文件代码结果")
@Data
public class MultiFileCodeResult {

    /**
     * html代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * css代码
     */
    @Description("CSS代码")
    private String cssCode;

    /**
     * js代码
     */
    @Description("JS代码")
    private String jsCode;

    /**
     * 描述
     */
    @Description("生成代码的描述")
    private String description;
}
