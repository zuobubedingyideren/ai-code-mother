package com.px.aicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * packageName: com.px.aicodemother.service
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: ProjectDownloadService
 * @date: 2025/9/25 09:51
 * @description: 下载项目服务层
 */
public interface ProjectDownloadService {
    /**
     * 将项目打包下载
     *
     * @param projectPath 项目路径
     * @param downloadFileName 下载文件名
     * @param response HttpServletResponse对象
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
