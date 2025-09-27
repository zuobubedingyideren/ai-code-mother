package com.px.aicodemother.langgraph4j.ai;

import com.px.aicodemother.langgraph4j.tools.ImageSearchTool;
import com.px.aicodemother.langgraph4j.tools.LogoGeneratorTool;
import com.px.aicodemother.langgraph4j.tools.MermaidDiagramTool;
import com.px.aicodemother.langgraph4j.tools.UndrawIllustrationTool;
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
 * @className: ImageCollectionServiceFactory
 * @date: 2025/9/26 23:57
 * @description: TODO
 */
@Slf4j
@Configuration
public class ImageCollectionServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Resource
    private ImageSearchTool imageSearchTool;

    @Resource
    private UndrawIllustrationTool undrawIllustrationTool;

    @Resource
    private MermaidDiagramTool mermaidDiagramTool;

    @Resource
    private LogoGeneratorTool logoGeneratorTool;

    /**
     * 创建图片收集服务
     *
     * @return 图片收集服务
     */
    @Bean
    public ImageCollectionService createImageCollectionService() {
        return AiServices.builder(ImageCollectionService.class)
                .chatModel(chatModel)
                .tools(
                        imageSearchTool,
                        undrawIllustrationTool,
                        mermaidDiagramTool,
                        logoGeneratorTool
                )
                .build();
    }
}
