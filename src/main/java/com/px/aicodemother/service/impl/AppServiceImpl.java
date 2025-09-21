package com.px.aicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.px.aicodemother.constants.AppConstant;
import com.px.aicodemother.core.AiCodeGeneratorFacade;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.exception.ThrowUtils;
import com.px.aicodemother.mapper.AppMapper;
import com.px.aicodemother.model.dto.app.AppQueryRequest;
import com.px.aicodemother.model.entity.App;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.enums.CodeGenTypeEnum;
import com.px.aicodemother.model.vo.app.AppVO;
import com.px.aicodemother.model.vo.user.UserVO;
import com.px.aicodemother.service.AppService;
import com.px.aicodemother.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author px
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

    @Resource
    private UserService userService;

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    /**
     * 根据实体查询VO转换
     *
     * @param app 实体
     * @return VO
     */
    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        Long userId = app.getUserId();
        // 获取关联的 User
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    /**
     * 根据查询条件构造QueryWrapper对象
     *
     * @param appQueryRequest 应用查询请求参数
     * @return QueryWrapper 查询条件构造器
     */
    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        // 校验请求参数
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        // 获取查询参数
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        // 创建查询条件构造器
        return QueryWrapper.create()
                .eq(App::getId, id)
                .like(App::getAppName, appName)
                .like(App::getCover, cover)
                .like(App::getInitPrompt, initPrompt)
                .eq(App::getCodeGenType, codeGenType)
                .eq(App::getDeployKey, deployKey)
                .eq(App::getPriority, priority)
                .eq(App::getUserId, userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    /**
     * 将应用实体列表转换为应用VO列表
     *
     * @param appList 应用实体列表
     * @return 应用VO列表
     */
    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }

        // 提取应用列表中所有用户ID
        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        // 批量获取用户信息并转换为用户VO映射
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        
        // 将应用实体转换为应用VO，并设置对应的用户信息
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUser(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据用户对话消息生成代码
     * 
     * @param appId 应用ID，不能为空且必须大于0
     * @param message 用户输入的对话消息，不能为空
     * @param loginUser 当前登录用户信息
     * @return 生成的代码内容流
     */
    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId错误");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");

        // 检查应用是否存在
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 检查用户是否有权限操作该应用
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作该应用");
        }

        // 获取并校验代码生成类型
        String codeGenType = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型：" + codeGenType);
        }

        // 调用AI代码生成器生成并保存代码
        return aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);

    }

    /**
     * 部署应用
     *
     * @param appId     应用ID
     * @param loginUser 当前登录用户
     * @return 部署后的访问地址
     */
    @Override
    public String deployApp(Long appId, User loginUser) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId错误");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录");

        // 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");

        // 检查用户是否有权限操作该应用
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作该应用");
        }

        // 检查是否有部署密钥
        String deployKey = app.getDeployKey();
        // 如果没有部署密钥，则生成一个随机密钥
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(6);
        }

        // 获取代码生成类型，构建应用源代码目录
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;

        // 检查源代码目录是否存在
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "源代码目录不存在");
        }

        // 构建应用部署目录
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "部署失败" + e.getMessage());
        }

        // 更新应用信息
        App updateApp = App.builder()
                .id(appId)
                .deployKey(deployKey)
                .deployedTime(LocalDateTime.now())
                .build();
        
        boolean result = this.updateById(updateApp);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新应用部署信息失败");

        // 返回应用访问地址
        return String.format("%s/%s/", AppConstant.CODE_DEPLOY_HOST, deployKey);
    }

}
