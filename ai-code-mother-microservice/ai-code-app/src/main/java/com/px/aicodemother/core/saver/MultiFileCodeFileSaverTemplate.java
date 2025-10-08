package com.px.aicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;

/**
 * packageName: com.px.aicodemother.core.saver
 *
 * @author: idpeng
 * @version: 1.0
 * @className: MultiFileCodeFileSaverTemplate
 * @date: 2025/9/18 23:30
 * @description: 多文件代码文件保存模板
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    /**
     * 获取代码类型
     *
     * @return 代码类型 MultiFileCode
     */
    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    /**
     * 保存代码文件
     *
     * @param result 代码结果
     */
    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // 保存 CSS 文件
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        // 保存 JavaScript 文件
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    /**
     * 验证输入参数
     *
     * @param result 输入参数
     */
    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // 至少要有 HTML 代码，CSS 和 JS 可以为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
