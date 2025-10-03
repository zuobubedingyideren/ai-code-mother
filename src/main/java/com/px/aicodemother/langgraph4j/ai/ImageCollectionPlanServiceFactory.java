package com.px.aicodemother.langgraph4j.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName: com.px.aicodemother.langgraph4j.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImageCollectionPlanServiceFactory
 * @date: 2025/9/30 09:40
 * @description: 图像收集计划服务工厂类，用于创建ImageCollectionPlanService实例
 */
@Configuration
public class ImageCollectionPlanServiceFactory {

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    /**
     * 创建图像收集计划服务实例
     * <p>
     * 使用AiServices构建ImageCollectionPlanService接口的实现，
     * 并配置所需的chatModel依赖
     * </p>
     * @return ImageCollectionPlanService 图像收集计划服务实例
     */
    @Bean
    public ImageCollectionPlanService createImageCollectionPlanService() {
        return AiServices.builder(ImageCollectionPlanService.class)
                .chatModel(chatModel)
                .build();
    }
}