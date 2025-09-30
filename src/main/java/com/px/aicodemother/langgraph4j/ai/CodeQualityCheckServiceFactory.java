package com.px.aicodemother.langgraph4j.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName: com.px.aicodemother.langgraph4j.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeQualityCheckServiceFactory
 * @date: 2025/9/29 20:51
 * @description: 代码质量检查服务工厂类，用于创建代码质量检查服务实例
 */
@Slf4j
@Configuration
public class CodeQualityCheckServiceFactory {

    @Resource
    private ChatModel chatModel;
    /**
     * 创建代码质量检查服务实例
     * 使用AiServices构建CodeQualityCheckService服务，配置chatModel作为聊天模型
     * 
     * @return CodeQualityCheckService 代码质量检查服务实例
     */
    @Bean
    public CodeQualityCheckService createCodeQualityCheckService() {
        return AiServices.builder(CodeQualityCheckService.class)
                .chatModel(chatModel)
                .build();
    }
}