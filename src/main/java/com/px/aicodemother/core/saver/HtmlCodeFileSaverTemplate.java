package com.px.aicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;

/**
 * packageName: com.px.aicodemother.core.saver
 *
 * @author: idpeng
 * @version: 1.0
 * @className: HtmlCodeFileSaverTemplate
 * @date: 2025/9/18 23:26
 * @description: HTML代码文件保存模板
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    /**
     * 获取代码类型
     *
     * @return 代码类型 HTML模式
     */
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    /**
     * 保存文件
     *
     * @param result      结果
     * @param baseDirPath 基础目录路径
     */
    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    /**
     * 验证输入参数
     *
     * @param result 结果
     */
    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
