package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.langgraph4j.tools.UndrawIllustrationTool;
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
 * @className: IllustrationCollectorNode
 * @date: 2025/9/30 10:56
 * @description: 插图图片收集节点
 */
@Slf4j
public class IllustrationCollectorNode {

    /**
     * 创建插图收集节点的异步操作
     * <p>
     * 该方法用于创建一个异步节点操作，负责并发收集插图资源。
     * 主要功能包括：
     * 1. 从工作流上下文中获取图片收集计划
     * 2. 根据计划中的插图任务并发搜索和收集插图
     * 3. 将收集到的插图资源存储到工作流上下文中
     * </p>
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作实例
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            List<ImageResource> illustrations = new ArrayList<>();
            try {
                ImageCollectionPlan plan = context.getImageCollectionPlan();
                if(plan != null && plan.getIllustrationTasks() != null) {
                    UndrawIllustrationTool illustrationTool = SpringContextUtil.getBean(UndrawIllustrationTool.class);
                    log.info("开始并发收集插图，任务数: {}", plan.getIllustrationTasks().size());
                    for (ImageCollectionPlan.IllustrationTask task : plan.getIllustrationTasks()) {
                        List<ImageResource> images = illustrationTool.searchIllustrations(task.query());
                        if (images !=  null) {
                            illustrations.addAll(images);
                        }
                    }
                    log.info("插图收集完成，共收集到 {} 张插图", illustrations.size());
                }
            } catch (Exception e) {
                log.error("插图收集失败: {}", e.getMessage(), e);
            }
            context.setIllustrations(illustrations);
            context.setCurrentStep("插画图片收集");
            return WorkflowContext.saveContext(context);
        });
    }
}
