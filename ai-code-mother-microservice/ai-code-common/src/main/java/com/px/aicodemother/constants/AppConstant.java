package com.px.aicodemother.constants;

/**
 * packageName: com.px.aicodemother.constants
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: AppConstant
 * @date: 2025/9/19 14:22
 * @description: 应用常量
 */
public interface AppConstant {

    /**
     * 精选应用的优先级
     */
    Integer GOOD_APP_PRIORITY = 99;

    /**
     * 默认应用优先级
     */
    Integer DEFAULT_APP_PRIORITY = 0;

    /**
     * 应用生成目录
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 应用部署目录
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * 应用部署域名
     */
    String CODE_DEPLOY_HOST = "http://localhost";

}
