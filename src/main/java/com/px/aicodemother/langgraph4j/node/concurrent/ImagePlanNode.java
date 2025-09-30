package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.ai.ImageCollectionPlanService;
import com.px.aicodemother.langgraph4j.model.ImageCollectionPlan;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

/**
 * packageName: com.px.aicodemother.langgraph4j.node.concurrent
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImagePlanNode
 * @date: 2025/9/30 10:40
 * @description: 图片规划节点
 */
@Slf4j
public class ImagePlanNode {

    /**
     * 创建图片规划节点的异步操作
     * <p>
     * 该方法用于创建一个异步节点操作，负责生成图片收集计划。
     * 主要功能包括：
     * 1. 从工作流上下文中获取原始提示词
     * 2. 调用AI服务生成图片收集计划
     * 3. 将生成的计划存储到工作流上下文中
     * </p>
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作实例
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            String originalPrompt = context.getOriginalPrompt();
            try {
                // 获取图片收集计划服务
                ImageCollectionPlanService planService = SpringContextUtil.getBean(ImageCollectionPlanService.class);
                ImageCollectionPlan plan = planService.planImageCollection(originalPrompt);
                log.info("生成图片收集计划，准备启动并发分支");
                // 将计划存储到上下文中
                context.setImageCollectionPlan(plan);
                context.setCurrentStep("图片计划");
            } catch (Exception e) {
                log.error("生成图片收集计划失败: {}", e.getMessage(), e);
            }
            return WorkflowContext.saveContext(context);
        });
    }
}
