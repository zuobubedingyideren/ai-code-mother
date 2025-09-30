package com.px.aicodemother.langgraph4j.ai;

import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * packageName: com.px.aicodemother.langgraph4j.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: ImageCollectionPlanService
 * @date: 2025/9/30 09:39
 * @description: 图片收集规划服务
 */
public interface ImageCollectionPlanService {
    @SystemMessage(fromResource = "prompt/image-collection-plan-system-prompt.txt")
    ImageCollectionPlan planImageCollection(@UserMessage String userPrompt);
}
