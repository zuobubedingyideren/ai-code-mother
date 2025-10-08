package com.px.aicodemother.ai.tools;


import cn.hutool.json.JSONObject;

/**
 * packageName: com.px.aicodemother.ai.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: BaseTool
 * @date: 2025/9/25 16:50
 * @description: 工具基类 定义所有工具的通用接口
 */
public abstract class BaseTool {

    /**
     * 获取工具名称
     *
     * @return 工具名称
     */
    public abstract String getToolName();

    /**
     * 获取工具显示名称
     *
     * @return 工具显示名称
     */
    public abstract String getDisplayName();

    /**
     * 生成工具请求响应
     *
     * @return 工具请求响应
     */
    public String generateToolRequestResponse() {
        return String.format("\n\n[选择工具] %s\n\n", getDisplayName());
    }

    /**
     * 生成工具执行结果
     *
     * @param arguments 工具参数
     * @return 工具执行结果
     */
    public abstract String generateToolExecutedResult(JSONObject arguments);
}
