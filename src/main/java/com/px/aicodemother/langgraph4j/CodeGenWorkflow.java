package com.px.aicodemother.langgraph4j;

import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.langgraph4j.node.*;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.prebuilt.MessagesStateGraph;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;

/**
 * packageName: com.px.aicodemother.langgraph4j
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeGenWorkflow
 * @date: 2025/9/27 11:11
 * @description: 代码生成工作流（实际可用）
 */
@Slf4j
public class CodeGenWorkflow {

    /**
     * 创建代码生成工作流
     * <p>
     * 该方法构建一个包含以下节点的有向图工作流：
     * 1. image_collector - 图片收集节点
     * 2. prompt_enhancer - 提示词增强节点
     * 3. router - 路由节点
     * 4. code_generator - 代码生成节点
     * 5. project_builder - 项目构建节点
     * </p>
     * <p>
     * 工作流执行顺序：
     * START → image_collector → prompt_enhancer → router → code_generator → project_builder → END
     * </p>
     *
     * @return 编译后的工作流图，类型为CompiledGraph<MessagesState<String>>
     * @throws BusinessException 当工作流创建失败时抛出业务异常
     */
    public CompiledGraph<MessagesState<String>> createWorkflow() {
        try {
            return new MessagesStateGraph<String>()
                    // 创建项目构建节点
                    .addNode("image_collector", ImageCollectorNode.create())
                    .addNode("prompt_enhancer", PromptEnhancerNode.create())
                    .addNode("router", RouterNode.create())
                    .addNode("code_generator", CodeGeneratorNode.create())
                    .addNode("project_builder", ProjectBuilderNode.create())

                    // 添加边
                    .addEdge(START, "image_collector")
                    .addEdge("image_collector", "prompt_enhancer")
                    .addEdge("prompt_enhancer", "router")
                    .addEdge("router", "code_generator")
                    .addEdge("code_generator", "project_builder")
                    .addEdge("project_builder", END)

                    // 编译工作流
                    .compile();
        } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "工作流创建失败");
        }
    }

    /**
     * 执行代码生成工作流
     * <p>
     * 该方法将执行完整的工作流，包括创建节点、连接边、执行各步骤并返回最终结果。
     * 工作流会按照预定义的顺序依次执行各个节点，并记录执行过程中的上下文信息。
     * </p>
     *
     * @param originalPrompt 原始提示词，用于启动代码生成工作流
     * @return WorkflowContext 最终的工作流上下文，包含执行结果和最终状态
     */
    public WorkflowContext executeWorkflow(String originalPrompt) {
        CompiledGraph<MessagesState<String>> workflow = createWorkflow();

        WorkflowContext initialContext = WorkflowContext.builder()
                .originalPrompt(originalPrompt)
                .currentStep("初始化")
                .build();

        GraphRepresentation graph = workflow.getGraph(GraphRepresentation.Type.MERMAID);
        log.info("工作流图:\n{}", graph.content());
        log.info("开始执行代码生成工作流");

        WorkflowContext finalContext = null;
        int stepCounter = 1;

        // 迭代执行工作流中的每个步骤
        for (NodeOutput<MessagesState<String>> step : workflow.stream(Map.of(WorkflowContext.WORKFLOW_CONTEXT_KEY, initialContext))) {
            log.info("--- 第 {} 步完成 ---", stepCounter);
            // 显示当前状态
            WorkflowContext currentContext = WorkflowContext.getContext(step.state());
            if (currentContext != null) {
                finalContext = currentContext;
                log.info("当前步骤上下文: {}", currentContext);
            }
            stepCounter++;
        }
        log.info("工作流执行完成！");
        return finalContext;
    }
}
