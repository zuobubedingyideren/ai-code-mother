package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.langgraph4j.tools.LogoGeneratorTool;
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
 * @className: LogoCollectorNode
 * @date: 2025/9/30 11:07
 * @description: Logo收集节点
 */
@Slf4j
public class LogoCollectorNode {

    /**
     * 创建Logo收集节点的异步操作
     * 
     * @return AsyncNodeAction<MessagesState<String>> 返回一个异步节点操作，用于处理Logo收集任务
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            List<ImageResource> logos = new ArrayList<>();

            try {
                ImageCollectionPlan plan = context.getImageCollectionPlan();
                // 检查计划和Logo任务是否存在
                if (plan != null && plan.getLogoTasks() != null) {
                    LogoGeneratorTool logoGeneratorTool = SpringContextUtil.getBean(LogoGeneratorTool.class);
                    log.info("开始并发收集Logo，任务数: {}", plan.getLogoTasks().size());
                    // 遍历所有Logo任务并生成Logo
                    for (ImageCollectionPlan.LogoTask task : plan.getLogoTasks()) {
                        List<ImageResource> images = logoGeneratorTool.generateLogos(task.description());
                        if (images != null) {
                            logos.addAll(images);
                        }
                    }
                    log.info("Logo收集完成，共收集到 {} 张图片", logos.size());
                }
            } catch (Exception e) {
                log.error("Logo收集失败: {}", e.getMessage(), e);
            }
            context.setLogos(logos);
            context.setCurrentStep("Logo生成");
            return WorkflowContext.saveContext(context);
        });
    }
}
