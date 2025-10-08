package com.px.aicodeuser.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.dto.user.UserQueryRequest;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.enums.UserRoleEnum;
import com.px.aicodemother.model.vo.user.UserVO;
import com.px.aicodeuser.mapper.UserMapper;
import com.px.aicodeuser.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.px.aicodemother.constants.UserConstant.USER_LOGIN_STATE;
import static com.px.aicodemother.constants.UserConstant.USER_SATT;

/**
 * 用户 服务层实现。
 *
 * @author px
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

    /**
     * 用户注册
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 新注册用户的ID
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 参数校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 检查账号是否重复
        long count = this.queryChain().eq(User::getUserAccount, userAccount).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 密码加密并构建用户对象
        String encryptPassword = this.getEncryptPassword(userPassword);

        User user = User.builder()
                .userAccount(userAccount)
                .userPassword(encryptPassword)
                .userName("新用户" + RandomUtil.randomNumbers(6))
                .userRole(UserRoleEnum.USER.getValue())
                .build();

        // 保存用户信息
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }

        return user.getId();
    }

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户原始密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        return DigestUtils.md5DigestAsHex((USER_SATT + userPassword).getBytes());
    }


    /**
     * 获取登录用户视图对象
     *
     * @param user 用户实体对象
     * @return 登录用户视图对象，如果用户为空则返回null
     */
    @Override
    public UserVO getLoginUserVo(User user) {
        if (user == null) {
            return null;
        }
        UserVO loginUserVo = new UserVO();
        BeanUtil.copyProperties(user, loginUserVo);
        return loginUserVo;
    }


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      HTTP请求对象
     * @return 登录用户视图对象
     */
    @Override
    public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 参数校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 密码加密
        String encryptPassword = this.getEncryptPassword(userPassword);

        // 查询用户
        User user = this.queryChain().eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword)
                .one();

        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 设置用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVo(user);
    }


    /**
     * 获取当前登录用户
     *
     * @param request HTTP请求对象
     * @return 当前登录用户对象
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 从Session中获取用户信息
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 查询最新用户信息
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 用户退出登录
     *
     * @param request HTTP请求对象
     * @return 退出登录是否成功
     * @throws BusinessException 当用户未登录时抛出异常
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        // 判断用户是否登录
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 将User实体对象转换为UserVO视图对象
     *
     * @param user 用户实体对象
     * @return 用户视图对象，如果传入的用户实体为空则返回null
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 将用户实体列表转换为用户视图对象列表
     *
     * @param userList 用户实体列表
     * @return 用户视图对象列表，如果传入的列表为空则返回空列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        return QueryWrapper.create()
                .eq(User::getId, id)
                .eq(User::getUserRole, userRole)
                .like(User::getUserAccount, userAccount)
                .like(User::getUserName, userName)
                .like(User::getUserProfile, userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));

    }
}
