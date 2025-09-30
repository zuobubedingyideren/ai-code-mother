package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.langgraph4j.tools.MermaidDiagramTool;
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
 * @className: DiagramCollectorNode
 * @date: 2025/9/30 11:03
 * @description: 架构图收集节点
 */
@Slf4j
public class DiagramCollectorNode {

    /**
     * 创建架构图收集节点的异步操作
     * <p>
     * 该方法用于创建一个异步节点操作，负责并发收集架构图资源。
     * 主要功能包括：
     * 1. 从工作流上下文中获取图片收集计划
     * 2. 根据计划中的架构图任务并发生成架构图
     * 3. 将生成的架构图资源存储到工作流上下文中
     * </p>
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作实例
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            List<ImageResource> diagrams = new ArrayList<>();

            try {
                ImageCollectionPlan plan = context.getImageCollectionPlan();
                if (plan != null && plan.getDiagramTasks() != null) {
                    MermaidDiagramTool diagramTool = SpringContextUtil.getBean(MermaidDiagramTool.class);
                    log.info("开始并发收集架构图，任务数: {}", plan.getDiagramTasks().size());
                    for (ImageCollectionPlan.DiagramTask task : plan.getDiagramTasks()) {
                        List<ImageResource> images = diagramTool.generateMermaidDiagram(task.mermaidCode(), task.description());
                        if (images != null) {
                            diagrams.addAll(images);
                        }
                    }
                    log.info("架构图收集完成，共收集到 {} 张图片", diagrams.size());
                }
            } catch (Exception e) {
                log.error("架构图收集失败: {}", e.getMessage(), e);
            }
            context.setDiagrams(diagrams);
            context.setCurrentStep("架构图生成");
            return WorkflowContext.saveContext(context);
        });
    }
}
