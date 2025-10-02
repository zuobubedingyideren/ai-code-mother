package com.px.aicodemother.langgraph4j;

import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.langgraph4j.model.QualityResult;
import com.px.aicodemother.langgraph4j.node.*;
import com.px.aicodemother.langgraph4j.node.concurrent.*;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.*;
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
 * @className: CodeGenSubgraphWorkflow
 * @date: 2025/9/30 14:08
 * @description: 子工作流
 */
@Slf4j
public class CodeGenSubgraphWorkflow {
    /**
     * 创建内容图片子图
     *
     * @return StateGraph<MessagesState<String>> 返回内容图片子图的状态图
     */
    private StateGraph<MessagesState<String>> createContentImageSubgraph() {
        try {
            return new MessagesStateGraph<String>()
                    .addNode("content_collect", ContentImageCollectorNode.create())
                    .addEdge(START, "content_collect")
                    .addEdge("content_collect", END);
        } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "内容图片子图创建失败");
        }
    }

    /**
     * 创建插图子图
     *
     * @return StateGraph<MessagesState<String>> 插图子图的状态图
     */
    public StateGraph<MessagesState<String>> createIllustrationSubgraph() {
        try {
            return new MessagesStateGraph<String>()
                    .addNode("illustration_collect", IllustrationCollectorNode.create())
                    .addEdge(START, "illustration_collect")
                    .addEdge("illustration_collect", END);
        } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "插图子图创建失败");
        }
    }

    /**
     * 创建架构图子图
     *
     * @return StateGraph<MessagesState<String>> 架构图子图状态图
     */
    public StateGraph<MessagesState<String>> createDiagramSubgraph() {
        try {
            return new MessagesStateGraph<String>()
                    .addNode("diagram_generate",DiagramCollectorNode.create())
                    .addEdge(START, "diagram_generate")
                    .addEdge("diagram_generate", END);
            } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "架构图子图创建失败");
        }
    }

    /**
     * 创建logo子图
     *
     * @return StateGraph<MessagesState<String>> logo子图状态图
     */
    public StateGraph<MessagesState<String>> createLogoSubgraph() {
        try {
            return new MessagesStateGraph<String>()
                    .addNode("logo_generate",DiagramCollectorNode.create())
                    .addEdge(START, "logo_generate")
                    .addEdge("logo_generate", END);
        } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "logo子图创建失败");
        }
    }

    public CompiledGraph<MessagesState<String>> createWorkflow() {
        try {
            // 获取各个未编译的子图（跟父图完全共享状态）
            StateGraph<MessagesState<String>> contentImageSubgraph = createContentImageSubgraph();
            StateGraph<MessagesState<String>> illustrationSubgraph = createIllustrationSubgraph();
            StateGraph<MessagesState<String>> diagramSubgraph = createDiagramSubgraph();
            StateGraph<MessagesState<String>> logoSubgraph = createLogoSubgraph();

            return new MessagesStateGraph<String>()
                    // 添加常规节点
                    .addNode("image_plan", ImagePlanNode.create())
                    .addNode("prompt_enhancer", PromptEnhancerNode.create())
                    .addNode("router", RouterNode.create())
                    .addNode("code_generator", CodeGeneratorNode.create())
                    .addNode("code_quality_check", CodeQualityCheckNode.create())
                    .addNode("project_builder", ProjectBuilderNode.create())

                    // 添加编译后的子图作为节点
                    .addNode("content_image_subgraph", contentImageSubgraph)
                    .addNode("illustration_subgraph", illustrationSubgraph)
                    .addNode("diagram_subgraph", diagramSubgraph)
                    .addNode("logo_subgraph", logoSubgraph)

                    // 添加图片聚合节点
                    .addNode("image_aggregator", ImageAggregatorNode.create())

                    // 并发子图分支：从计划节点分发到各个子图
                    .addEdge(START, "image_plan")
                    .addEdge("image_plan", "content_image_subgraph")
                    .addEdge("image_plan", "illustration_subgraph")
                    .addEdge("image_plan", "diagram_subgraph")
                    .addEdge("image_plan", "logo_subgraph")

                    // 汇聚：所有子图都汇聚到聚合器
                    .addEdge("content_image_subgraph", "image_aggregator")
                    .addEdge("illustration_subgraph", "image_aggregator")
                    .addEdge("diagram_subgraph", "image_aggregator")
                    .addEdge("logo_subgraph", "image_aggregator")

                    // 继续串行流程
                    .addEdge("image_aggregator", "prompt_enhancer")
                    .addEdge("prompt_enhancer", "router")
                    .addEdge("router", "code_generator")
                    .addEdge("code_generator", "code_quality_check")

                    // 质检条件边
                    .addConditionalEdges("code_quality_check",
                            edge_async(this::routeAfterQualityCheck),
                            Map.of(
                                    "build", "project_builder",
                                    "skip_build", END,
                                    "fail", "code_generator"
                            ))
                    .addEdge("project_builder", END)

                    .compile();
        } catch (GraphStateException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "子图工作流创建失败");
        }
    }

    /**
     * 质量检查后的路由决策方法
     * 
     * @param state 消息状态对象，包含工作流上下文信息
     * @return String 返回路由决策结果："fail"表示质检失败需要重新生成，"build"表示需要构建项目，"skip_build"表示跳过构建
     */
    private String routeAfterQualityCheck(MessagesState<String> state) {
        WorkflowContext context = WorkflowContext.getContext(state);
        QualityResult qualityResult = context.getQualityResult();

        // 检查代码质量检查结果是否有效
        if (qualityResult == null || !qualityResult.getIsValid()) {
            log.error("代码质检失败，需要重新生成代码");
            return "fail";
        }

        log.info("代码质检通过，继续后续流程");
        CodeGenTypeEnum generationType = context.getGenerationType();
        // 根据生成类型决定是否需要构建项目
        if (generationType == CodeGenTypeEnum.VUE_PROJECT) {
            return "build";
        } else {
            return "skip_build";
        }
    }

    /**
     * 执行子图代码生成工作流
     *
     * @param originalPrompt 原始提示
     * @return WorkflowContext 工作流上下文对象
     */
    public WorkflowContext executeWorkflow(String originalPrompt) {
        CompiledGraph<MessagesState<String>> workflow = createWorkflow();

        WorkflowContext initialContext = WorkflowContext.builder()
                .originalPrompt(originalPrompt)
                .currentStep("初始化")
                .build();

        GraphRepresentation graph = workflow.getGraph(GraphRepresentation.Type.MERMAID);
        log.info("子图工作流图:\n{}", graph.content());
        log.info("开始执行子图代码生成工作流");

        WorkflowContext finalContext = null;
        int stepCounter = 1;

        for (NodeOutput<MessagesState<String>> step : workflow.stream(
                Map.of(WorkflowContext.WORKFLOW_CONTEXT_KEY, initialContext))) {
            log.info("--- 第 {} 步完成 ---", stepCounter);
            WorkflowContext currentContext = WorkflowContext.getContext(step.state());
            if (currentContext != null) {
                finalContext = currentContext;
                log.info("当前步骤上下文: {}", currentContext);
            }
            stepCounter++;
        }
        log.info("子图代码生成工作流执行完成！");
        return finalContext;
    }

}
