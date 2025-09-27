package com.px.aicodemother.langgraph4j.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.node
 *
 * @author: idpeng
 * @version: 1.0
 * @className: PromptEnhancerNode
 * @date: 2025/9/26 14:07
 * @description: 提示词增强节点
 */
@Slf4j
public class PromptEnhancerNode {

    /**
     * 创建提示词增强节点的异步操作
     * <p>
     * 该方法会获取上下文中的原始提示词和图片资源列表，将它们组合成增强的提示词，
     * 并更新上下文中的当前步骤和增强提示词字段。
     * </p>
     *
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作对象，用于处理消息状态
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            log.info("执行节点: 提示词增强");

            String originalPrompt = context.getOriginalPrompt();
            String imageListStr = context.getImageListStr();
            List<ImageResource> imageList = context.getImageList();

            StringBuilder enhancedPromptBuilder = new StringBuilder();
            enhancedPromptBuilder.append(originalPrompt);
            // 如果存在图片资源，则在提示词中添加图片资源说明
            if (CollUtil.isNotEmpty(imageList) || StrUtil.isNotBlank(imageListStr)) {
                enhancedPromptBuilder.append("\n\n## 可用素材资源\n");
                enhancedPromptBuilder.append("请在生成网站使用以下图片资源，将这些图片合理地嵌入到网站的相应位置中。\n");
                if (CollUtil.isNotEmpty(imageList)) {
                    // 遍历图片资源列表，将每个图片的分类、描述和URL添加到提示词中
                    for (ImageResource image : imageList) {
                        enhancedPromptBuilder.append("-")
                                .append(image.getCategory().getText())
                                .append(": ")
                                .append(image.getDescription())
                                .append("(")
                                .append(image.getUrl())
                                .append(")");
                    }
                } else {
                    enhancedPromptBuilder.append(imageListStr);
                }
            }

            String enhancedPrompt = enhancedPromptBuilder.toString();
            
            // 更新数据
            context.setCurrentStep("提示词增强");
            context.setEnhancedPrompt(enhancedPrompt);
            log.info("提示词增强完成");
            return WorkflowContext.saveContext(context);
        });
    }
}