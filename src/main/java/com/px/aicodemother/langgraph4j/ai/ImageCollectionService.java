package com.px.aicodemother.langgraph4j.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * packageName: com.px.aicodemother.langgraph4j.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: ImageCollectionService
 * @date: 2025/9/26 23:53
 * @description: 图片收集 AI 服务接口 使用 AI 调用工具收集不同类型的图片资源
 */
public interface ImageCollectionService {

    /**
     * 收集图片资源
     * @param userPrompt 用户输入的提示词
     * @return 图片资源列表
     */
    @SystemMessage(fromResource = "prompt/image-collection-system-prompt.txt")
    String collectImages(@UserMessage String userPrompt);
}
