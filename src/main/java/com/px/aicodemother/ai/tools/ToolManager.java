package com.px.aicodemother.ai.tools;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName: com.px.aicodemother.ai.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ToolManager
 * @date: 2025/9/25 17:08
 * @description: 工具管理器 统一管理所有工具，提供根据名称获取工具的功能
 */
@Slf4j
@Component
public class ToolManager {

    /**
     * 工具映射表，用于根据工具名称获取工具实例
     */
    private final Map<String, BaseTool> toolMap = new HashMap<>();

    /**
     * 工具列表，用于初始化工具映射表
     */
    @Resource
    private BaseTool[] tools;

    /**
     * 初始化工具列表，将工具添加到工具映射表中
     */
    @PostConstruct
    public void initTools() {
        for (BaseTool tool : tools) {
            toolMap.put(tool.getToolName(), tool);
            log.info("注册工具: {} -> {}", tool.getToolName(), tool.getDisplayName());
        }
        log.info("工具管理器初始化完成，共注册 {} 个工具", toolMap.size());
    }

    /**
     * 根据工具名称获取工具实例
     * @param toolName 工具英文名称
     * @return 工具实例
     */
    public BaseTool getTool(String toolName) {
        return toolMap.get(toolName);
    }

    /**
     * 获取所有工具列表
     * @return 所有工具列表
     */
    public BaseTool[] getAllTools() {
    	return tools;
    }
}
