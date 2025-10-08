package com.px.aicodemother.core.parser;

import com.px.aicodemother.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName: com.px.aicodemother.core.parser
 *
 * @author: idpeng
 * @version: 1.0
 * @className: HtmlCodeParser
 * @date: 2025/9/18 23:10
 * @description: HTML单文件代码解析器
 */
public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {

    /**
     * HTML代码块的正则表达式模式
     */
    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /**
     * 解析 HTML 代码
     *
     * @param codeContent 源代码内容
     * @return 解析html结果
     */
    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult result = new HtmlCodeResult();
        // 提取 HTML 代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        } else {
            // 如果没有找到代码块，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
        }
        return result;
    }

    /**
     * 提取 HTML 代码块
     *
     * @param content 源代码内容
     * @return HTML 代码块
     */
    private String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
