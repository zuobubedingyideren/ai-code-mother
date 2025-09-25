package com.px.aicodemother.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.px.aicodemother.annotation.AuthCheck;
import com.px.aicodemother.common.BaseResponse;
import com.px.aicodemother.common.DeleteRequest;
import com.px.aicodemother.common.ResultUtils;
import com.px.aicodemother.constants.AppConstant;
import com.px.aicodemother.constants.UserConstant;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.exception.ThrowUtils;
import com.px.aicodemother.model.dto.app.*;
import com.px.aicodemother.model.entity.App;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.vo.app.AppVO;
import com.px.aicodemother.service.AppService;
import com.px.aicodemother.service.ProjectDownloadService;
import com.px.aicodemother.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 应用 控制层。
 *
 * @author px
 */
@RestController
@RequestMapping("/app")
@Tag(name = "应用", description = "应用接口")
public class AppController {

    @Resource
    private AppService appService;

    @Resource
    private UserService userService;

    @Resource
    private ProjectDownloadService projectDownloadService;

    /**
     * 添加应用
     *
     * @param appAddRequest 应用添加请求参数，包含初始化提示词等信息
     * @param request       HTTP请求对象，用于获取当前登录用户信息
     * @return 返回添加成功的应用ID
     */
    @PostMapping("/add")
    @Operation(summary = "添加应用", description = "添加应用",
            parameters = {
                @Parameter(name = "appAddRequest", description = "应用添加请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Long> addApp(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        Long appId = appService.createApp(appAddRequest, loginUser);
        return ResultUtils.success(appId);
    }

    /**
     * 更新应用
     *
     * @param appUpdateRequest 应用更新请求参数，包含应用ID和应用名称等信息
     * @param request          HTTP请求对象，用于获取当前登录用户信息
     * @return 返回更新结果，成功返回true
     */
    @PostMapping("/update")
    @Operation(summary = "更新应用", description = "更新应用",
            parameters = {
                @Parameter(name = "appUpdateRequest", description = "应用更新请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        // 检查请求参数和应用ID是否有效
        if (appUpdateRequest == null || appUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        Long id = appUpdateRequest.getId();

        // 获取原应用信息
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 检查是否有权限更新该应用
        if (!oldApp.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 构建更新后的应用对象
        App app = App.builder()
                .id(id)
                .appName(appUpdateRequest.getAppName())
                .editTime(LocalDateTime.now())
                .build();
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除应用
     *
     * @param deleteRequest 删除请求参数，包含要删除的应用ID
     * @param request       HTTP请求对象，用于获取当前登录用户信息
     * @return 返回删除结果，成功返回true
     */
    @PostMapping("/delete")
    @Operation(summary = "删除应用", description = "删除应用",
            parameters = {
                @Parameter(name = "deleteRequest", description = "删除请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        // 检查请求参数和应用ID是否有效
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        
        // 获取原应用信息
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 检查是否有权限删除该应用
        if (!oldApp.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 执行删除操作
        boolean result = appService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据应用ID获取应用视图对象
     *
     * @param id 应用ID
     * @return 应用视图对象
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取应用信息", description = "获取应用信息",
            parameters = {@Parameter(name = "id", description = "应用ID")})
    public BaseResponse<AppVO> getAppVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 获取应用信息
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 构建并返回应用视图对象
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 分页获取当前用户创建的应用列表
     *
     * @param appQueryRequest 应用查询请求参数，包含分页信息和查询条件
     * @param request HTTP请求对象，用于获取当前登录用户信息
     * @return 分页的应用VO列表
     */
    @PostMapping("/my/list/page/vo")
    @Operation(summary = "获取用户应用列表（分页）", description = "获取用户应用列表（分页）",
            parameters = {
                @Parameter(name = "appQueryRequest", description = "应用查询请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Page<AppVO>> listMyAppVOByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);

        // 校验并获取分页参数
        int pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR, "单页条数不能超过20");
        int pageNum = appQueryRequest.getPageNum();

        // 设置查询条件：只查询当前用户创建的应用
        appQueryRequest.setUserId(loginUser.getId());

        // 执行分页查询
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);

        // 转换为VO对象并返回
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 分页获取精选应用列表
     * 精选应用是指优先级设置为GOOD_APP_PRIORITY(99)的应用
     *
     * @param appQueryRequest 精选应用查询请求参数，包含分页信息和查询条件
     * @return 分页的精选应用VO列表
     */
    @PostMapping("/good/list/page/vo")
    @Operation(summary = "获取精选应用列表（分页）", description = "获取精选应用列表（分页）",
            parameters = {
                @Parameter(name = "appQueryRequest", description = "精选应用查询请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Page<AppVO>> listGoodAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 校验并获取分页参数
        int pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR, "单页条数不能超过20");
        int pageNum = appQueryRequest.getPageNum();
        
        // 设置查询条件：只查询精选应用（优先级为99）
        appQueryRequest.setPriority(AppConstant.GOOD_APP_PRIORITY);
        
        // 执行分页查询
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        
        // 转换为VO对象并返回
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }


    /**
     * 管理员删除应用
     *
     * @param deleteRequest 删除请求参数，包含应用ID
     * @return 删除结果
     */
    @PostMapping("/admin/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除应用(管理员)", description = "删除应用仅管理员可用",
            parameters = {
                @Parameter(name = "deleteRequest", description = "删除请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Boolean> deleteAppByAdmin(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        Long id = deleteRequest.getId();
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = appService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新应用信息
     * 仅管理员可调用此接口，可更新应用名称、封面和优先级等信息
     *
     * @param appAdminUpdateRequest 应用更新请求参数，包含应用ID、应用名称、封面和优先级等信息
     * @return 更新结果，成功返回true
     */
    @PostMapping("/admin/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新应用(管理员)", description = "更新应用仅管理员可用",
            parameters = {
                @Parameter(name = "appAdminUpdateRequest", description = "应用更新请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Boolean> updateAppByAdmin(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        // 参数校验
        if (appAdminUpdateRequest == null || appAdminUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取应用ID并检查应用是否存在
        Long id = appAdminUpdateRequest.getId();
        
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 复制属性并设置编辑时间
        App app = new App();
        BeanUtil.copyProperties(appAdminUpdateRequest, app);
        app.setEditTime(LocalDateTime.now());
        
        // 执行更新操作
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分页获取应用列表
     * 仅管理员可调用此接口，可查询所有用户创建的应用
     *
     * @param appQueryRequest 应用查询请求参数，包含分页信息和查询条件
     * @return 分页的应用VO列表
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "获取应用列表（管理员）", description = "获取应用列表（管理员）",
            parameters = {
                @Parameter(name = "appQueryRequest", description = "应用查询请求参数"),
                @Parameter(name = "request", description = "请求")})
    public BaseResponse<Page<AppVO>> listAppVOByPageByAdmin(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取分页参数
        int pageNum = appQueryRequest.getPageNum();
        int pageSize = appQueryRequest.getPageSize();
        
        // 执行分页查询
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize),
                appService.getQueryWrapper(appQueryRequest));
        
        // 转换为VO对象并返回
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 管理员根据应用ID获取应用信息
     * 仅管理员可调用此接口，可获取任意应用的详细信息
     *
     * @param id 应用ID
     * @return 应用视图对象
     */
    @GetMapping("/admin/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "获取应用信息", description = "获取应用信息",
            parameters = {@Parameter(name = "id", description = "应用ID")})
    public BaseResponse<AppVO> getAppVOByIdByAdmin(long id) {
        // 参数校验
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        // 获取应用信息
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 转换为VO对象并返回
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 通过对话方式生成代码
     * 
     * @param appId 应用ID，必须大于0
     * @param message 用户输入的消息，不能为空
     * @param request HTTP请求对象，用于获取登录用户信息
     * @return 生成的代码内容流
     */
    @GetMapping(value = "/chat/gen/code", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "生成代码", description = "获取应用信息",
            parameters = {
                @Parameter(name = "appId", description = "应用ID"),
                @Parameter(name = "message", description = "用户消息"),
                @Parameter(name = "request", description = "请求")
    })
    public Flux<ServerSentEvent<String>> chatToGenCode(@RequestParam Long appId,
                                      @RequestParam String message,
                                      HttpServletRequest request) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId错误");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 调用服务层方法生成代码
        Flux<String> contentFlux = appService.chatToGenCode(appId, message, loginUser);

        return contentFlux
                .map(chunk -> {
                    // 包装数据成json
                            Map<String, String> wrapper = Map.of("d", chunk);
                            String jsonData = JSONUtil.toJsonStr(wrapper);
                            return ServerSentEvent.<String>builder()
                                    .data(jsonData)
                                    .build();
                        }
                )
                .concatWith(Mono.just(
                        // 发送一个done事件，表示生成完成
                        ServerSentEvent.<String>builder()
                                .event("done")
                                .data("")
                                .build()
                ));
    }



    /**
     * 部署应用程序
     *
     * @param appDeployRequest 应用部署请求参数，包含要部署的应用ID等信息
     * @param request          HTTP请求对象，用于获取当前登录用户信息
     * @return 部署成功后的URL地址
     */
    @PostMapping("/deploy")
    @Operation(summary = "部署应用", description = "部署应用",
            parameters = {
                    @Parameter(name = "appDeployRequest", description = "应用部署请求参数"),
                    @Parameter(name = "request", description = "请求")})
    public BaseResponse<String> deployApp(@RequestBody AppDeployRequest appDeployRequest, HttpServletRequest request) {
        // 参数校验
        ThrowUtils.throwIf(appDeployRequest == null, ErrorCode.PARAMS_ERROR);

        Long appId = appDeployRequest.getAppId();
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId错误");

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 调用服务执行应用部署
        String deployUrl = appService.deployApp(appId, loginUser);
        return ResultUtils.success(deployUrl);
    }

    /**
     * 下载应用代码
     *
     * @param appId 应用ID
     * @param request HTTP请求对象，用于获取当前登录用户信息
     * @param response HTTP响应对象，用于返回下载的文件
     */
    @GetMapping("/download/{appId}")
    @Operation(summary = "下载应用代码", description = "下载应用代码",
            parameters = {
                    @Parameter(name = "appId", description = "应用ID"),
                    @Parameter(name = "request", description = "请求"),
                    @Parameter(name = "response", description = "响应")
            })
    public void downloadAppCode(@PathVariable Long appId, HttpServletRequest request, HttpServletResponse response) {
        // 基础教育
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId错误");
        // 查询应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");

        // 验证权限：只有应用创建者和管理员可以下载
        User loginUser = userService.getLoginUser(request);
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,  "无权限下载该应用代码");
        }

        // 构建应用代码目录路径
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;

        // 检查代码目录是否存在
        File sourceDir = new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists() || !sourceDir.isDirectory(), ErrorCode.NOT_FOUND_ERROR, "应用代码不存在，请先生成代码");

        // 创建下载文件
        String downloadFileName = String.valueOf(appId);

        // 调用通用下载服务
        projectDownloadService.downloadProjectAsZip(sourceDirPath, downloadFileName, response);
    }

}
