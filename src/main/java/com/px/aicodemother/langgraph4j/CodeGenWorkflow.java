package com.px.aicodemother.langgraph4j;

import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.langgraph4j.model.QualityResult;
import com.px.aicodemother.langgraph4j.node.*;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
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
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;

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
                    .addNode("code_quality_check", CodeQualityCheckNode.create())
                    .addNode("project_builder", ProjectBuilderNode.create())

                    // 添加边
                    .addEdge(START, "image_collector")
                    .addEdge("image_collector", "prompt_enhancer")
                    .addEdge("prompt_enhancer", "router")
                    .addEdge("router", "code_generator")
                    .addEdge("code_generator", "code_quality_check")
                    // 新增质检条件边：根据质检结果决定下一步
                    .addConditionalEdges("code_quality_check",
                            edge_async(this::routeAfterQualityCheck),
                            Map.of(
                                    "build", "project_builder",   // 质检通过且需要构建
                                    "skip_build", END,            // 质检通过但跳过构建
                                    "fail", "code_generator"      // 质检失败，重新生成
                            ))
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

    /**
     * 根据代码生成类型决定是否跳过项目构建步骤
     * 
     * @param state 工作流状态对象，包含当前处理的消息状态
     * @return "skip_build"表示跳过构建，"build"表示执行构建
     */
    private String routeAfterQualityCheck(MessagesState<String> state) {
        WorkflowContext context = WorkflowContext.getContext(state);
        QualityResult qualityResult = context.getQualityResult();
        // 如果质检失败，重新生成代码
        if (qualityResult == null || !qualityResult.getIsValid()) {
            log.error("代码质检失败，需要重新生成代码");
            return "fail";
        }
        // 质检通过，使用原有的构建路由逻辑
        log.info("代码质检通过，继续后续流程");
        return routeBuildOrSkip(state);
    }

    /**
     * 根据代码生成类型决定是否需要构建
     *
     * @param state 工作流状态对象，包含当前处理消息的状态
     * @return "skip_build"表示跳过构建，"build"表示执行构建
     */
    private String routeBuildOrSkip(MessagesState<String> state) {
        WorkflowContext context = WorkflowContext.getContext(state);
        CodeGenTypeEnum generationType = context.getGenerationType();
        // HTML 和 MULTI_FILE 类型不需要构建，直接结束
        if (generationType == CodeGenTypeEnum.HTML || generationType == CodeGenTypeEnum.MULTI_FILE) {
            return "skip_build";
        }
        // VUE_PROJECT 需要构建
        return "build";
    }
}
