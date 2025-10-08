package com.px.aicodemother.service;

/**
 * packageName: com.px.aicodemother.service
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: ScreenshotService
 * @date: 2025/9/24 09:59
 * @description:
 */
public interface ScreenshotService {

    /**
     * 生成并上传截图
     *
     * @param webUrl 要截取的网页URL
     * @return 截图的URL，失败返回null
     */
    String generateAndUploadScreenshot(String webUrl);

}
