package com.px.aicodemother.ai;

import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

/**
 * packageName: com.px.aicodemother.ai
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: AiCodeGeneratorService
 * @date: 2025/9/18 16:32
 * @description: 描述
 */
public interface AiCodeGeneratorService {

    /**
     * 生成html代码
     *
     * @param userMessage 用户信息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);


    /**
     * 生成多个文件代码
     *
     * @param userMessage 用户信息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * 生成html代码流
     *
     * @param userMessage 用户信息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 生成多个文件代码流
     *
     * @param userMessage 用户信息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);
}
