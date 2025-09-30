package com.px.aicodemother.langgraph4j.ai;

import com.px.aicodemother.langgraph4j.model.QualityResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * packageName: com.px.aicodemother.langgraph4j.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeQualityCheckService
 * @date: 2025/9/29 20:33
 * @description: 代码质量检查服务
 */
public interface CodeQualityCheckService {
    /**
     * 检查代码质量
     * AI 会分析代码并返回质量检查结果
     */
    @SystemMessage(fromResource = "prompt/code-quality-check-system-prompt.txt")
    QualityResult checkCodeQuality(@UserMessage String codeContent);
}
