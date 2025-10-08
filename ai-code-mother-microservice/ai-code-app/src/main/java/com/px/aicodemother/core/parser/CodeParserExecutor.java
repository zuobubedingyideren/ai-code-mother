package com.px.aicodemother.core.parser;

import com.px.aicodemother.model.enums.CodeGenTypeEnum;

/**
 * packageName: com.px.aicodemother.core.parser
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeParserExecutor
 * @date: 2025/9/18 23:15
 * @description: 代码解析执行器,根据代码生成类型执行相应的解析逻辑
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> {
                String errorMsg = "不支持的生成类型：" + codeGenType.getValue();
                throw new RuntimeException(errorMsg);
            }
        };
    }
}
