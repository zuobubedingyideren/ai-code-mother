package com.px.aicodemother.core;

import com.px.aicodemother.ai.AiCodeGeneratorService;
import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
import com.px.aicodemother.core.parser.CodeParserExecutor;
import com.px.aicodemother.core.saver.CodeFileSaverExecutor;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * packageName: com.px.aicodemother.core
 *
 * @author: idpeng
 * @version: 1.0
 * @className: AiCodeGeneratorFacade
 * @date: 2025/9/18 21:28
 * @description: Ai代码生成器外观类
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 根据用户消息和代码生成类型生成并保存代码
     *
     * @param userMessage 用户提供的生成要求消息
     * @param codeGenType 代码生成类型枚举
     * @return 保存代码文件的目录File对象
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenType, Long appId) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenType) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield  CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMsg = "不支持的生成类型：" + codeGenType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        };
    }

    /**
     * 根据用户消息和代码生成类型生成并保存代码
     *
     * @param userMessage 用户提供的生成要求消息
     * @param codeGenType 代码生成类型枚举
     * @return 流式返回的代码片段
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenType, Long appId) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenType) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenType, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenType, appId);
            }
            default -> {
                String errorMsg = "不支持的生成类型：" + codeGenType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        };
    }

    /**
     * 处理代码流，在流完成时解析并保存代码
     *
     * @param codeStream  代码流数据
     * @param codeGenType 代码生成类型枚举
     * @param appId
     * @return 原始代码流
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        // 收集完整的代码内容
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream
                // 添加代码片段到代码内容中
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String completeCode = codeBuilder.toString();
                        // 解析代码内容
                        Object parserResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                        // 保存解析后的代码
                        File saveFile = CodeFileSaverExecutor.executeSaver(parserResult, codeGenType, appId);
                        log.info("代码保存成功：{}", saveFile.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("代码保存失败：{}", e.getMessage());
                    }
                });
    }
}
