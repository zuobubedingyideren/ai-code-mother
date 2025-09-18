package com.px.aicodemother.core.saver;

import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * packageName: com.px.aicodemother.core.saver
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeFileSaverExecutor
 * @date: 2025/9/18 23:32
 * @description: 代码保存执行器
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}
