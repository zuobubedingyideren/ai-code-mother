package com.px.aicodemother.ai;

import com.px.aicodemother.utils.SpringContextUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName: com.px.aicodemother.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiCodeGenTypeRoutingServiceFactory
 * @date: 2025/9/25 14:04
 * @description: ai代码生成类型路由服务工厂类
 */
@Slf4j
@Configuration
public class AiCodeGenTypeRoutingServiceFactory {


    /**
     * 创建ai代码生成类型路由服务实例
     *
     * @return AiCodeGenTypeRoutingService ai代码生成类型路由服务实例
     */
    public AiCodeGenTypeRoutingService createAiCodeGenTypeRoutingService() {
        ChatModel chatModel = SpringContextUtil.getBean("routingChatModelPrototype", ChatModel.class);
        return AiServices.builder(AiCodeGenTypeRoutingService.class)
                .chatModel(chatModel)
                .build();
    }

    /**
     * 创建ai代码生成类型路由服务实例
     *
     * @return AiCodeGenTypeRoutingService ai代码生成类型路由服务实例
     */
    @Bean
    public AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService() {
        return createAiCodeGenTypeRoutingService();
    }
}
