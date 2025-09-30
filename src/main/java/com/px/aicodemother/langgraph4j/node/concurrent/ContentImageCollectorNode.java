package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.langgraph4j.tools.ImageSearchTool;
import com.px.aicodemother.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.node.concurrent
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ContentImageCollectorNode
 * @date: 2025/9/30 10:49
 * @description: 内容图片收集节点
 */
@Slf4j
public class ContentImageCollectorNode {

    /**
     * 创建内容图片收集节点的异步操作
     * <p>
     * 该方法用于创建一个异步节点操作，负责并发收集内容相关的图片资源。
     * 主要功能包括：
     * 1. 从工作流上下文中获取图片收集计划
     * 2. 根据计划中的任务并发搜索和收集内容图片
     * 3. 将收集到的图片资源存储到工作流上下文中
     * </p>
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作实例
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            List<ImageResource> contentImages = new ArrayList<>();
            try {
                ImageCollectionPlan plan = context.getImageCollectionPlan();
                if (plan != null && plan.getContentImageTasks() != null) {
                    ImageSearchTool imageSearchTool = SpringContextUtil.getBean(ImageSearchTool.class);
                    log.info("开始并发收集内容图片，任务数: {}", plan.getContentImageTasks().size());
                    for (ImageCollectionPlan.ImageSearchTask task : plan.getContentImageTasks()) {
                        List<ImageResource> images = imageSearchTool.searchContentImages(task.query());
                        if (images != null) {
                            contentImages.addAll(images);
                        }
                    }
                    log.info("内容图片收集完成，共收集到 {} 张图片", contentImages.size());
                }
            } catch (Exception e) {
                log.error("内容图片收集失败: {}", e.getMessage(), e);
            }
            // 将收集到的图片存储到上下文的中间字段中
            context.setContentImages(contentImages);
            context.setCurrentStep("内容图片收集");
            return WorkflowContext.saveContext(context);
        });
    }
}
