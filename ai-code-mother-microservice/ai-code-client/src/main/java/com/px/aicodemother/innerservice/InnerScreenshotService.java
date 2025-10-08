package com.px.aicodemother.innerservice;

/**
 * packageName: com.px.aicodemother.innerservice
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: InnerScreenshotService
 * @date: 2025/10/8 15:52
 * @description: 内部截图服务接口
 */
public interface InnerScreenshotService {

    /**
     * 生成并上传截图
     *
     * @param webUrl 网页URL
     * @return 截图URL
     */
    String generateAndUploadScreenshot(String webUrl);
}
