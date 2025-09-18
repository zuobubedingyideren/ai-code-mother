package com.px.aicodemother.core;

import com.px.aicodemother.ai.AiCodeGeneratorService;
import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
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
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenType) {
            case HTML -> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
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
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenType) {
            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default -> {
                String errorMsg = "不支持的生成类型：" + codeGenType.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        };
    }

    /**
     * 根据用户消息生成并保存HTML代码
     *
     * @param userMessage 用户提供的生成要求消息
     * @return 保存代码文件的目录File对象
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    /**
     * 根据用户消息生成并保存多文件代码
     * @param userMessage 用户提供的生成要求消息
     * @return 保存代码文件的目录File对象
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

    /**
     * 流式生成并保存HTML代码
     * 
     * @param userMessage 用户提供的生成要求消息
     * @return 流式返回的代码片段
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return result
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeHtmlCode = codeBuilder.toString();
                        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败: {}", e.getMessage());
                    }
                });
    }

    /**
     * 流式生成并保存多文件代码（HTML + CSS + JS）
     * 
     * @param userMessage 用户提供的生成要求消息
     * @return 流式返回的代码片段
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return result
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeMultiFileCode = codeBuilder.toString();
                        MultiFileCodeResult multiFileResult = CodeParser.parseMultiFileCode(completeMultiFileCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveMultiFileCodeResult(multiFileResult);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败: {}", e.getMessage());
                    }
                });
    }
}
