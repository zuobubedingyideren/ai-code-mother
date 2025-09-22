package com.px.aicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.px.aicodemother.annotation.AuthCheck;
import com.px.aicodemother.common.BaseResponse;
import com.px.aicodemother.common.ResultUtils;
import com.px.aicodemother.constants.UserConstant;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.exception.ThrowUtils;
import com.px.aicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.px.aicodemother.model.entity.ChatHistory;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.service.ChatHistoryService;
import com.px.aicodemother.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对话历史 控制层。
 *
 * @author px
 */
@RestController
@RequestMapping("/chatHistory")
@Tag(name = "对话历史", description = "对话历史控制层")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private UserService userService;


    /**
     * 获取指定应用的对话历史列表
     *
     * @param appId 应用ID
     * @param pageSize 分页大小，默认为10
     * @param lastCreateTime 游标查询时间，查询该时间之前的数据
     * @param request HTTP请求对象，用于获取当前登录用户
     * @return 分页的对话历史列表
     */
    @GetMapping("/app/{appId}")
    @Operation(summary = "获取应用下的对话历史", description = "获取应用下的对话历史",
            parameters = {
                    @Parameter(name = "appId", description = "应用id"),
                    @Parameter(name = "pageSize", description = "分页大小"),
                    @Parameter(name = "lastCreateTime", description = "游标查询 - 最后一条记录的创建时间")
    })
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) LocalDateTime lastCreateTime,
                                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * 管理员分页获取所有对话历史列表
     *
     * @param chatHistoryQueryRequest 对话历史查询请求参数
     * @return 分页的对话历史列表
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员获取所有对话历史列表（分页）", description = "管理员获取所有对话历史列表（分页）",
            parameters = {@Parameter(name = "chatHistoryQueryRequest", description = "查询请求"),})
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }

    
}
