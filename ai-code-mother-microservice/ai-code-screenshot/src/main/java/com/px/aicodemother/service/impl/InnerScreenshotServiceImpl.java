package com.px.aicodemother.service.impl;

import com.px.aicodemother.innerservice.InnerScreenshotService;
import com.px.aicodemother.service.ScreenshotService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * packageName: com.px.aicodemother.service.impl
 *
 * @author: idpeng
 * @version: 1.0
 * @className: InnerScreenshotServiceImpl
 * @date: 2025/10/8 17:51
 * @description: 截图服务实现类
 */
@DubboService
public class InnerScreenshotServiceImpl implements InnerScreenshotService {
    @Resource
    private ScreenshotService screenshotService;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        return screenshotService.generateAndUploadScreenshot(webUrl);
    }
}
