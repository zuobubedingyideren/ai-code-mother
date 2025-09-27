package com.px.aicodemother.langgraph4j.node;

import com.px.aicodemother.constants.AppConstant;
import com.px.aicodemother.core.AiCodeGeneratorFacade;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import com.px.aicodemother.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * packageName: com.px.aicodemother.langgraph4j.node
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeGeneratorNode
 * @date: 2025/9/26 14:17
 * @description: 代码生成节点, 使用AI进行工具调用，生成代码
 */
@Slf4j
public class CodeGeneratorNode {

    /**
     * 创建代码生成节点的异步操作
     * 
     * @return AsyncNodeAction<MessagesState<String>> 异步节点操作对象，处理完成后返回更新的状态
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state -> {
            WorkflowContext context = WorkflowContext.getContext(state);
            log.info("执行节点: 代码生成");

            String userMessage = context.getEnhancedPrompt();
            CodeGenTypeEnum generationType = context.getGenerationType();

            // 获取代码生成器门面服务
            AiCodeGeneratorFacade codeGeneratorFacade = SpringContextUtil.getBean(AiCodeGeneratorFacade.class);
            log.info("开始生成代码，类型: {} ({})", generationType.getValue(), generationType.getText());

            Long appId = 0L;
            // 调用AI代码生成服务生成代码流
            Flux<String> codeStream = codeGeneratorFacade.generateAndSaveCodeStream(userMessage, generationType, appId);

            // 阻塞等待代码生成完成，超时时间10分钟
            codeStream.blockLast(Duration.ofMinutes(10));

            String generatedCodeDir = String.format("%s/%s_%s", AppConstant.CODE_OUTPUT_ROOT_DIR, generationType.getValue(), appId);
            log.info("AI 代码生成完成，生成目录: {}", generatedCodeDir);

            context.setCurrentStep("代码生成");
            context.setGeneratedCodeDir(generatedCodeDir);
            return WorkflowContext.saveContext(context);
        });
    }
}