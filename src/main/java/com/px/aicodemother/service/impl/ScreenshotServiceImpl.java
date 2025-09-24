package com.px.aicodemother.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.exception.ThrowUtils;
import com.px.aicodemother.manager.CosManager;
import com.px.aicodemother.service.ScreenshotService;
import com.px.aicodemother.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * packageName: com.px.aicodemother.service.impl
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ScreenshotServiceImpl
 * @date: 2025/9/24 10:08
 * @description: 截图服务实现类
 */
@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private CosManager cosManager;

    /**
     * 生成并上传截图
     *
     * @param webUrl 要截取的网页URL
     * @return 截图的URL，失败返回null
     */
    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "网页URL不能为空");
        log.info("开始生成网页截图，URL: {}", webUrl);
        // 1. 生成本地截图
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath), ErrorCode.OPERATION_ERROR, "本地截图生成失败");
        try {
            // 2. 上传到对象存储
            String cosUrl = uploadScreenshotToCos(localScreenshotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl), ErrorCode.OPERATION_ERROR, "截图上传对象存储失败");
            log.info("网页截图生成并上传成功: {} -> {}", webUrl, cosUrl);
            return cosUrl;
        } finally {
            // 3. 清理本地文件
            cleanupLocalFile(localScreenshotPath);
        }
    }

    /**
     * 上传截图文件到 COS
     *
     * @param localScreenshotPath 本地截图文件路径
     * @return 截图的访问 URL，失败返回 null
     */
    private String uploadScreenshotToCos(String localScreenshotPath) {
        if (StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }

        File screenshotFile = new File(localScreenshotPath);
        if (!screenshotFile.exists()) {
            log.error("截图文件不存在: {}", localScreenshotPath);
            return null;
        }

        // 生成 COS 对象键
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String cosKey = generateScreenshotKey(fileName);
        return cosManager.uploadFile(cosKey, screenshotFile);
    }

    /**
     * 生成截图对象键
     * @param fileName 文件名
     * @return 对象键
     */
    private String generateScreenshotKey(String fileName)
    {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"
        ));
        return String.format("/screenshots/%s/%s"
                , datePath, fileName);
    }

    /**
     * 清理本地截图文件
     * @param localFilePath 本地截图文件路径
     */
    private void cleanupLocalFile(String localFilePath)
    {
        File localFile = new File
                (localFilePath);
        if
        (localFile.exists()) {
            File parentDir =
                    localFile.getParentFile();
            FileUtil.del(parentDir);
            log.info(
                    "本地截图文件已清理: {}"
                    , localFilePath);
        }
    }
}
