package com.px.aicodemother.ai.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

/**
 * packageName: com.px.aicodemother.ai.guardrail
 *
 * @author: idpeng
 * @version: 1.0
 * @className: RetryOutputGuardrail
 * @date: 2025/10/4 17:07
 * @description: 模型输出内容安全检查
 */
public class RetryOutputGuardrail implements OutputGuardrail {

    /**
     * 验证AI模型输出内容的安全性和完整性
     * 
     * 对AI模型的输出进行检查，包括内容是否为空、是否过短以及是否包含敏感信息，
     * 如果检查不通过则要求重新生成内容。
     * 
     * @param responseFromLLM AI模型的响应消息对象
     * @return 输出检查结果，如果检查通过则返回成功结果，否则返回重新提示结果并包含错误信息
     */
    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String response = responseFromLLM.text();

        // 检查响应内容是否为空
        if (response == null || response.trim().isEmpty()) {
            return reprompt("响应内容为空", "请重新生成完整的内容");
        }

        // 检查响应内容是否过短
        if (response.trim().length() < 10) {
            return reprompt("响应内容过短", "请提供更详细的内容");
        }
        
        // 检查是否包含敏感信息或不当内容
        if (containsSensitiveContent(response)) {
            return reprompt("包含敏感信息", "请重新生成内容，避免包含敏感信息");
        }
        return success();
    }
    /**
     * 检查响应内容是否包含敏感信息或不当内容
     *
     * @param response 响应内容
     * @return 如果包含敏感信息或不当内容，则返回 true；否则返回 false
     */
    private boolean containsSensitiveContent(String response) {
        String lowerResponse = response.toLowerCase();
        String[] sensitiveWords = {
                "密码", "password", "secret", "token",
                "api key", "私钥", "证书", "credential"
        };
        for (String word : sensitiveWords) {
            if (lowerResponse.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
