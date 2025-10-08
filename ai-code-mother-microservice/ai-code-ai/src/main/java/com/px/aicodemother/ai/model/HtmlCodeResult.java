package com.px.aicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * packageName: com.px.aicodemother.ai.model
 *
 * @author: idpeng
 * @version: 1.0
 * @className: HtmlCodeResult
 * @date: 2025/9/18 21:07
 * @description: 单文件返回结果封装类
 */
@Description("生成 HTML 代码文件的结果")
@Data
public class HtmlCodeResult {

    /**
     * html代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * 描述
     */
    @Description("生成代码的描述")
    private String description;
}
