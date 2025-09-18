package com.px.aicodemother.core;

import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("任务记录网站", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("任务记录网站", CodeGenTypeEnum.MULTI_FILE);
        
        // 流式处理，实时打印每个片段
        codeStream
            .doOnNext(chunk -> {
                System.out.print(chunk); // 实时打印每个代码片段
                System.out.flush();
            })
            .doOnComplete(() -> {
                System.out.println("\n=== 流式输出完成 ===");
            })
            .doOnError(error -> {
                System.err.println("流式输出出错: " + error.getMessage());
            })
            .blockLast(); // 只阻塞等待最后一个元素，但过程中会实时输出
    }
}