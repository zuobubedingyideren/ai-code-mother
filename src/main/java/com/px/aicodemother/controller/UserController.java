package com.px.aicodemother.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.px.aicodemother.annotation.AuthCheck;
import com.px.aicodemother.common.BaseResponse;
import com.px.aicodemother.common.DeleteRequest;
import com.px.aicodemother.common.ResultUtils;
import com.px.aicodemother.constants.UserConstant;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.exception.ThrowUtils;
import com.px.aicodemother.model.dto.user.*;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.vo.user.UserVO;
import com.px.aicodemother.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author px
 */
@Tag(name = "用户模块", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 新用户id
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册",
        parameters = {
            @Parameter(name = "userRegisterRequest", description = "用户注册请求参数"),
        })
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR );
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request 请求
     * @return 登录用户脱敏信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录",
        parameters = {
            @Parameter(name = "userLoginRequest", description = "用户登录请求参数"),
            @Parameter(name = "request", description = "请求")
        })
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR );
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        UserVO result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request HTTP请求对象
     * @return 当前登录用户的视图对象
     */
    @GetMapping("/get/login")
    @Operation(summary = "获取当前登录用户", description = "获取当前登录用户", parameters = {@Parameter(name = "request", description = "请求")})
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR );
        // 获取当前登录用户并转换为视图对象
        User result = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVo(result));
    }

    /**
     * 用户注销
     *
     * @param request HTTP请求对象
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "用户注销", parameters = {@Parameter(name = "request", description = "请求")})
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR );
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest 用户添加请求参数
     * @return 新添加用户的ID
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "添加用户", description = "添加用户(仅管理员）",
        parameters = {
            @Parameter(name = "userAddRequest", description = "用户添加请求参数"),
        })
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);

        // 设置默认密码并加密
        String encryptPassword = userService.getEncryptPassword(UserConstant.DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }


    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "获取用户", description = "获取用户（仅管理员）", parameters = {@Parameter(name = "id", description = "用户ID")})
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据用户ID获取用户视图对象
     *
     * @param id 用户ID
     * @return 用户视图对象
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取用户视图对象", description = "获取用户视图对象", parameters = {@Parameter(name = "id", description = "用户ID")})
    public BaseResponse<UserVO> getUserVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        UserVO userVO = userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }


    /**
     * 删除用户
     *
     * @param deleteRequest 删除请求参数
     * @return 删除结果
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除用户", description = "删除用户（仅管理员）",
        parameters = {
            @Parameter(name = "deleteRequest", description = "删除请求参数"),
        })
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求参数
     * @return 更新结果
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新用户", description = "更新用户（仅管理员）",
        parameters = {
            @Parameter(name = "userUpdateRequest", description = "用户更新请求参数"),
        })
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest == null || userUpdateRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户视图对象列表
     *
     * @param userQueryRequest 用户查询请求参数
     * @return 用户视图对象分页列表
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "分页获取用户列表（仅管理员）", description = "分页获取用户列表（仅管理员）",
        parameters = {
            @Parameter(name = "userQueryRequest", description = "用户查询请求参数"),
        })
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        int pageNum = userQueryRequest.getPageNum();
        int pageSize = userQueryRequest.getPageSize();
        // 根据查询条件分页获取用户数据
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getQueryWrapper(userQueryRequest));

        // 转换为用户视图对象
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }
}