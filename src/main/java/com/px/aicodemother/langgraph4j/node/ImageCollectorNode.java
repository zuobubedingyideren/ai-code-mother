package com.px.aicodemother.langgraph4j.node;

import com.px.aicodemother.langgraph4j.ai.ImageCollectionService;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * packageName: com.px.aicodemother.langgraph4j.node
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImageCollectorNode
 * @date: 2025/9/26 13:57
 * @description: 图片收集节点, 使用AI进行工具调用，收集不同类型的图片
 */
@Slf4j
public class ImageCollectorNode {
    /**
     * 创建图片收集节点的异步操作
     * 
     * @return AsyncNodeAction<MessagesState<String>> 返回一个异步节点操作，用于处理消息状态
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            log.info("执行节点: 图片收集");
            String originalPrompt = context.getOriginalPrompt();
            String imageListStr = "";

           try {
                log.info("开始执行图片收集节点，原始提示词: {}", originalPrompt);
                
                // 获取图片收集服务
                ImageCollectionService imageCollectionService = SpringContextUtil.getBean(ImageCollectionService.class);
                
                // 调用图片收集服务 - 这里会自动处理工具调用和响应
                imageListStr = imageCollectionService.collectImages(originalPrompt);
                
                log.info("图片收集完成，结果: {}", imageListStr);
                
            } catch (Exception e) {
                log.error("图片收集失败: {}", e.getMessage(), e);
                // 设置默认值，避免流程中断
                imageListStr = "图片收集失败，将使用默认图片资源";
            }
            // 更新状态
            context.setCurrentStep("图片收集");
            context.setImageListStr(imageListStr);
            return WorkflowContext.saveContext(context);
        });
    }
}
