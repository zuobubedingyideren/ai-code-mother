package com.px.aicodemother.controller;

import com.px.aicodemother.langgraph4j.CodeGenWorkflow;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * packageName: com.px.aicodemother.controller
 *
 * @author: idpeng
 * @version: 1.0
 * @className: WorkflowSseController
 * @date: 2025/10/2 15:25
 * @description: 工作流 SSE 控制器 演示 LangGraph4j 工作流的流式输出功能
 */
@RestController
@RequestMapping("/workflow")
@Slf4j
@Tag(name = "工作流 SSE 控制器", description = "演示 LangGraph4j 工作流的流式输出功能")
public class WorkflowSseController {

    /**
     * 同步执行工作流
     * <p>
     * 该方法接收一个提示词参数，创建并执行代码生成工作流，
     * 返回工作流执行完成后的最终上下文信息。
     * </p>
     *
     * @param prompt 工作流输入参数，用于指导代码生成的提示词
     * @return WorkflowContext 工作流执行完成后的上下文信息
     */
    @PostMapping("/execute")
    @Operation(summary = "执行工作流", description = "执行工作流",
                parameters = {@Parameter(name = "prompt", description = "工作流输入参数")})
    public WorkflowContext executeWorkflow(@RequestParam String prompt) {
        log.info("收到同步工作流执行请求: {}", prompt);
        return new CodeGenWorkflow().executeWorkflow(prompt);
    }

    /**
     * 使用 Flux 异步执行工作流
     * <p>
     * 该方法接收一个提示词参数，创建并执行基于 Flux 的代码生成工作流，
     * 通过 Server-Sent Events (SSE) 流式返回工作流执行过程中的实时信息。
     * </p>
     *
     * @param prompt 工作流输入参数，用于指导代码生成的提示词
     * @return Flux<String> 返回包含工作流执行事件的 Flux 流，每个事件都是格式化的 SSE 字符串
     */
    @GetMapping(value = "/execute-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "执行工作流", description = "执行工作流",
                parameters = {@Parameter(name = "prompt", description = "工作流输入参数")})
    public Flux<String> executeWorkflowWithFlux(@RequestParam String prompt) {
        log.info("收到 Flux 工作流执行请求: {}", prompt);
        return new CodeGenWorkflow().executeWorkflowWithFlux(prompt);
    }

    /**
     * 使用 SseEmitter 异步执行工作流
     * <p>
     * 该方法接收一个提示词参数，创建并执行基于 SseEmitter 的代码生成工作流，
     * 通过 Server-Sent Events (SSE) 流式返回工作流执行过程中的实时信息。
     * </p>
     *
     * @param prompt 工作流输入参数，用于指导代码生成的提示词
     * @return SseEmitter 返回 SSE 发送器，用于向客户端推送工作流执行事件
     */
    @GetMapping(value = "/execute-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "执行工作流", description = "执行工作流",
                parameters = {@Parameter(name = "prompt", description = "工作流输入参数")})
    public SseEmitter executeWorkflowWithSse(@RequestParam String prompt) {
        log.info("收到 SSE 工作流执行请求: {}", prompt);
        return new CodeGenWorkflow().executeWorkflowWithSse(prompt);
    }

}
