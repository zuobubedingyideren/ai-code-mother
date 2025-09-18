package com.px.aicodemother.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.px.aicodemother.ai.model.HtmlCodeResult;
import com.px.aicodemother.ai.model.MultiFileCodeResult;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * packageName: com.px.aicodemother.core
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CodeFileSaver
 * @date: 2025/9/18 21:23
 * @description: 代码文件保存工具类
 */
public class CodeFileSaver {

    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存HtmlCodeResult到文件系统
     *
     * @param result HtmlCodeResult对象，包含需要保存的HTML代码
     * @return 保存文件的目录File对象
     */
    public static File saveHtmlCodeResult(HtmlCodeResult result) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存MultiFileCodeResult到文件系统
     *
     * @param result MultiFileCodeResult对象，包含需要保存的HTML、CSS和JS代码
     * @return 保存文件的目录File对象
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult result) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        return new File(baseDirPath);
    }

    /**
     * 构建一个唯一的目录路径：tmp/code_output/bizType_雪花ID
     *
     * @param bizType 业务类型
     * @return 唯一的目录名称
     */
    private static String buildUniqueDir(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 将内容写入文件
     *
     * @param dirPath    目录路径
     * @param filename   文件名
     * @param content    文件内容
     */
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
