package com.px.aicodemother.core.parser;

/**
 * packageName: com.px.aicodemother.core.parser
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: CodeParser
 * @date: 2025/9/18 23:08
 * @description: 代码解析器策略接口
 */
public interface CodeParser<T> {

    /**
     * 解析代码
     * @param codeContent 代码内容
     * @return 解析结果
     */
    T parseCode(String codeContent);
}
