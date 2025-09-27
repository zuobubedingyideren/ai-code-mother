package com.px.aicodemother.langgraph4j.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.langgraph4j.enums.ImageCategoryEnum;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.manager.CosManager;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName: com.px.aicodemother.langgraph4j.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: MermaidDiagramTool
 * @date: 2025/9/26 17:11
 * @description: Mermaid 架构图生成工具
 */
@Slf4j
@Component
public class MermaidDiagramTool {

    @Resource
    private CosManager cosManager;

    /**
     * Chrome 浏览器可执行文件路径（可选配置）
     * 如果不配置，将使用 Puppeteer 默认的 Chromium
     */
    @Value("${mermaid.chrome.executable.path:}")
    private String chromeExecutablePath;

    /**
     * Puppeteer 超时时间（毫秒）
     */
    @Value("${mermaid.puppeteer.timeout:30000}")
    private int puppeteerTimeout;

    /**
     * 将 Mermaid 代码转换为架构图图片，用于展示系统结构和技术关系
     * @param mermaidCode Mermaid 图表代码
     * @param description 架构图描述
     * @return 图片资源列表
     */
    @Tool("将 Mermaid 代码转换为架构图图片，用于展示系统结构和技术关系")
    public List<ImageResource> generateMermaidDiagram(@P("Mermaid 图表代码") String mermaidCode, @P("架构图描述") String description) {
        // 检查输入的Mermaid代码是否为空
        if (StrUtil.isBlank(mermaidCode)) {
            return new ArrayList<>();
        }
        try {
            // 转换为SVG图片
            File diagramFile = convertMermaidToSvg(mermaidCode);

            // 生成文件存储路径并上传到COS
            String keyName = String.format("/mermaid/%s/%s",
                    RandomUtil.randomString(5), diagramFile.getName());
            String cosUrl = cosManager.uploadFile(keyName, diagramFile);
            // 删除本地临时文件
            FileUtil.del(diagramFile);
            // 如果上传成功，构建并返回图片资源对象
            if (StrUtil.isNotBlank(cosUrl)) {
                return Collections.singletonList(ImageResource.builder()
                        .category(ImageCategoryEnum.ARCHITECTURE)
                        .description(description)
                        .url(cosUrl)
                        .build());
            }
        } catch (Exception e) {
            log.error("生成架构图失败: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 将Mermaid代码转换为SVG图片文件
     * @param mermaidCode Mermaid图表代码
     * @return 生成的SVG图片文件
     */
    private File convertMermaidToSvg(String mermaidCode) {
        // 创建临时输入文件并写入Mermaid代码
        File tempInputFile = FileUtil.createTempFile("mermaid_input_", ".mmd", true);
        FileUtil.writeUtf8String(mermaidCode, tempInputFile);

        // 创建临时输出文件
        File tempOutputFile = FileUtil.createTempFile("mermaid_output_", ".svg", true);

        // 创建 Puppeteer 配置文件（如果需要）
        File puppeteerConfigFile = createPuppeteerConfigFile();

        // 根据操作系统确定命令名称
        String command = SystemUtil.getOsInfo().isWindows() ? "mmdc.cmd" : "mmdc";

        // 构造执行命令
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(command)
                .append(" -i ").append(tempInputFile.getAbsolutePath())
                .append(" -o ").append(tempOutputFile.getAbsolutePath())
                .append(" --outputFormat svg")
                .append(" -b transparent");

        // 如果有 Puppeteer 配置文件，添加到命令中
        if (puppeteerConfigFile != null) {
            cmdBuilder.append(" --puppeteerConfigFile ").append(puppeteerConfigFile.getAbsolutePath());
            log.info("使用自定义 Chrome 浏览器路径: {}", chromeExecutablePath);
        }

        String cmdLine = cmdBuilder.toString();

        try {
            // 执行Mermaid CLI命令并捕获输出
            String result = RuntimeUtil.execForStr(cmdLine);
            log.debug("Mermaid CLI 执行结果: {}", result);
            
            // 检查输出文件是否存在且非空
            if (!tempOutputFile.exists() || tempOutputFile.length() == 0) {
                log.error("Mermaid CLI 执行失败，输出文件不存在或为空。命令: {}", cmdLine);
                String errorMsg = "Mermaid CLI 执行失败，可能是因为缺少 Chrome 浏览器。";
                if (StrUtil.isBlank(chromeExecutablePath)) {
                    errorMsg += "请运行: npx puppeteer browsers install chrome-headless-shell，或配置 mermaid.chrome.executable.path 属性指定 Chrome 路径";
                } else {
                    errorMsg += "请检查配置的 Chrome 路径是否正确: " + chromeExecutablePath;
                }
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
            }
        } catch (Exception e) {
            log.error("执行 Mermaid CLI 命令时发生异常: {}", e.getMessage(), e);
            // 删除临时文件
            FileUtil.del(tempInputFile);
            FileUtil.del(tempOutputFile);
            if (puppeteerConfigFile != null) {
                FileUtil.del(puppeteerConfigFile);
            }
            
            String errorMsg = "Mermaid CLI 执行失败: " + e.getMessage();
            if (StrUtil.isBlank(chromeExecutablePath)) {
                errorMsg += "。请确保已安装 Mermaid CLI 和 Chrome 浏览器，或配置 mermaid.chrome.executable.path 属性";
            } else {
                errorMsg += "。请检查配置的 Chrome 路径: " + chromeExecutablePath;
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMsg);
        }

        // 删除临时文件
        FileUtil.del(tempInputFile);
        if (puppeteerConfigFile != null) {
            FileUtil.del(puppeteerConfigFile);
        }
        return tempOutputFile;
    }

    /**
     * 验证 Chrome 可执行文件路径是否有效
     * @param executablePath Chrome 可执行文件路径
     * @return 是否有效
     */
    private boolean isValidChromeExecutable(String executablePath) {
        if (StrUtil.isBlank(executablePath)) {
            return false;
        }
        
        File chromeFile = new File(executablePath);
        boolean isValid = chromeFile.exists() && chromeFile.isFile() && chromeFile.canExecute();
        
        if (!isValid) {
            log.warn("Chrome 可执行文件路径无效: {}", executablePath);
        }
        
        return isValid;
    }

    /**
     * 创建 Puppeteer 配置文件
     * @return 配置文件对象，如果不需要配置则返回 null
     */
    private File createPuppeteerConfigFile() {
        // 如果没有配置 Chrome 路径，则不创建配置文件
        if (StrUtil.isBlank(chromeExecutablePath)) {
            log.debug("未配置 Chrome 路径，将使用 Puppeteer 默认的 Chromium");
            return null;
        }

        // 验证 Chrome 路径是否有效
        if (!isValidChromeExecutable(chromeExecutablePath)) {
            log.error("配置的 Chrome 路径无效，将使用 Puppeteer 默认的 Chromium: {}", chromeExecutablePath);
            return null;
        }

        try {
            // 创建配置 Map
            Map<String, Object> config = new HashMap<>();
            config.put("executablePath", chromeExecutablePath);
            config.put("timeout", puppeteerTimeout);
            
            // 在 Windows 上添加一些常用的启动参数
            if (SystemUtil.getOsInfo().isWindows()) {
                List<String> args = new ArrayList<>();
                args.add("--no-sandbox");
                args.add("--disable-setuid-sandbox");
                args.add("--disable-dev-shm-usage");
                args.add("--disable-gpu");
                config.put("args", args);
            }

            // 创建临时配置文件
            File configFile = FileUtil.createTempFile("puppeteer_config_", ".json", true);
            String configJson = JSONUtil.toJsonPrettyStr(config);
            FileUtil.writeUtf8String(configJson, configFile);
            
            log.debug("创建 Puppeteer 配置文件: {}, 内容: {}", configFile.getAbsolutePath(), configJson);
            return configFile;
        } catch (Exception e) {
            log.warn("创建 Puppeteer 配置文件失败，将使用默认配置: {}", e.getMessage(), e);
            return null;
        }
    }
}
