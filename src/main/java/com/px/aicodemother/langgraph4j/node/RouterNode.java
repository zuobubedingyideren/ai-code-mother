package com.px.aicodemother.langgraph4j.node;

import com.px.aicodemother.ai.AiCodeGenTypeRoutingService;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import com.px.aicodemother.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

/**
 * packageName: com.px.aicodemother.langgraph4j.node
 *
 * @author: idpeng
 * @version: 1.0
 * @className: RouterNode
 * @date: 2025/9/26 14:12
 * @description: 智能路由节点
 */
@Slf4j
public class RouterNode {

    /**
     * 创建一个异步节点操作，用于根据用户提示智能选择代码生成类型
     * 
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作对象，处理完成后返回更新的状态
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            log.info("执行节点: 智能路由");

            CodeGenTypeEnum codeGenTypeEnum;

            // 尝试使用AI服务进行代码生成类型的智能路由
            try {
                AiCodeGenTypeRoutingService routingService = SpringContextUtil.getBean(AiCodeGenTypeRoutingService.class);
                codeGenTypeEnum = routingService.routeCodeGenType(context.getOriginalPrompt());
                log.info("AI智能路由完成，选择类型: {} ({})", codeGenTypeEnum.getValue(), codeGenTypeEnum.getText());
            } catch (Exception e) {
                log.error("AI智能路由失败: {}", e.getMessage(), e);
                codeGenTypeEnum = CodeGenTypeEnum.HTML;
            }

            context.setCurrentStep("智能路由");
            context.setGenerationType(codeGenTypeEnum);
            return WorkflowContext.saveContext(context);
        });
    }
}