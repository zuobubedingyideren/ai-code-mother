package com.px.aicodemother.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName: com.px.aicodemother.config
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ReasoningStreamingChatModelConfig
 * @date: 2025/9/22 14:53
 * @description: 推理流式模型配置
 */
@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
@Data
public class ReasoningStreamingChatModelConfig {

    private String baseUrl;

    private String apiKey;

    /**
     * 推理流式模型（用于 Vue 项目生成，带工具调用）
     * @return 推理流式模型
     */
    @Bean
    public StreamingChatModel reasoningStreamingChatModel() {
        // 为了测试方便临时修改
        final String modelName = "deepseek-chat";
        final int maxTokens = 8192;
        // 生产环境使用：
        // final String modelName = "deepseek-reasoner";
        // final int maxTokens = 32768;
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
