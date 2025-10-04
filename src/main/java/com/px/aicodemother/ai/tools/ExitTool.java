package com.px.aicodemother.ai.tools;

import cn.hutool.json.JSONObject;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * packageName: com.px.aicodemother.ai.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ExitTool
 * @date: 2025/10/4 17:12
 * @description: TODO
 */
@Slf4j
@Component
public class ExitTool extends BaseTool{
    @Override
    public String getToolName() {
        return "exit";
    }

    @Override
    public String getDisplayName() {
        return "退出工具调用";
    }

    @Override
    public String generateToolExecutedResult(JSONObject arguments) {
        return "\n\n[执行结束]\n\n";
    }

    /**
     * 退出工具调用
     * 
     * 当AI完成任务或判断无需继续调用其他工具时，调用此方法来结束工具调用流程，
     * 防止出现无限循环调用的情况。该方法会记录日志并返回提示信息。
     * 
     * @return 提示信息，告知AI可以输出最终结果了
     */
    @Tool("当任务已完成或无需继续调用工具时，使用此工具退出操作，防止循环")
    public String exit() {
        log.info("AI 请求退出工具调用");
        return "不要继续调用工具，可以输出最终结果了";
    }
}
