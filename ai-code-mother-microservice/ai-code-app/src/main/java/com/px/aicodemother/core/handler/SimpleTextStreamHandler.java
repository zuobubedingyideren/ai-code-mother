package com.px.aicodemother.core.handler;

import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.px.aicodemother.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * packageName: com.px.aicodemother.core.handler
 *
 * @author: idpeng
 * @version: 1.0
 * @className: SimpleTextStreamHandler
 * @date: 2025/9/23 11:15
 * @description: 简单文本流处理器，用于处理AI生成的文本流并记录对话历史
 */
@Slf4j
public class SimpleTextStreamHandler {

    /**
     * 处理原始文本流，在流完成时记录AI回复到对话历史
     *
     * @param originFlux 原始文本流
     * @param chatHistoryService 对话历史服务
     * @param appId 应用ID
     * @param loginUser 当前登录用户
     * @return 处理后的文本流
     */
    public Flux<String> handle(Flux<String> originFlux,
                               ChatHistoryService chatHistoryService,
                               long appId, User loginUser) {
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originFlux
                .map(chunk -> {
                    // 收集AI响应内容
                    aiResponseBuilder.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    // 流式响应完成后，添加AI消息到对话历史
                    String aiResponse = aiResponseBuilder.toString();
                    chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                })
                .doOnError(error -> {
                    // 如果AI回复失败，也要记录错误消息
                    String errorMessage = "AI回复失败: " + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }
}