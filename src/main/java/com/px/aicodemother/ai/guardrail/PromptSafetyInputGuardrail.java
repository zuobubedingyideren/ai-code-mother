package com.px.aicodemother.ai.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * packageName: com.px.aicodemother.ai.guardrail
 *
 * @author: idpeng
 * @version: 1.0
 * @className: PromptSafetyInputGuardrail
 * @date: 2025/10/4 16:32
 * @description: TODO
 */
public class PromptSafetyInputGuardrail implements InputGuardrail {

    /**
     * 敏感词列表
     */
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
            "忽略之前的指令", "ignore previous instructions", "ignore above",
            "破解", "hack", "绕过", "bypass", "越狱", "jailbreak"
    );

    /**
     * 注入式指令列表
     */
    private static final List<Pattern> INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("(?i)ignore\\s+(?:previous|above|all)\\s+(?:instructions?|commands?|prompts?)"),
            Pattern.compile("(?i)(?:forget|disregard)\\s+(?:everything|all)\\s+(?:above|before)"),
            Pattern.compile("(?i)(?:pretend|act|behave)\\s+(?:as|like)\\s+(?:if|you\\s+are)"),
            Pattern.compile("(?i)system\\s*:\\s*you\\s+are"),
            Pattern.compile("(?i)new\\s+(?:instructions?|commands?|prompts?)\\s*:")
    );

    /**
     * 验证用户输入的安全性
     * 
     * 对用户输入进行多方面检查，包括长度限制、空内容检查、敏感词过滤和注入攻击模式检测，
     * 以防止恶意输入和不当内容。
     * 
     * @param userMessage 包含用户输入消息的对象
     * @return 输入检查结果，如果检查通过则返回成功结果，否则返回失败结果并包含错误信息
     */
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String input = userMessage.singleText();
        // 检查输入长度是否超过限制
        if (input.length() > 1000) {
            return fatal("输入内容过长，不要超过 1000 字");
        }
        // 检查输入是否为空或只包含空白字符
        if (input.trim().isEmpty()) {
            return fatal("输入内容不能为空");
        }
        // 检查是否包含敏感词
        String lowerInput = input.toLowerCase();
        for (String sensitiveWord : SENSITIVE_WORDS) {
            if (lowerInput.contains(sensitiveWord.toLowerCase())) {
                return fatal("输入包含不当内容，请修改后重试");
            }
        }
        // 检查是否匹配注入攻击模式
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return fatal("检测到恶意输入，请求被拒绝");
            }
        }
        return success();
    }
}
