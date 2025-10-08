package com.px.aicodemother.innerservice;

import com.px.aicodemother.exception.BusinessException;
import com.px.aicodemother.exception.ErrorCode;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static com.px.aicodemother.constants.UserConstant.USER_LOGIN_STATE;

/**
 * packageName: com.px.aicodemother.innerservice
 *
 * @author: idpeng
 * @version: 1.0
 * @className: InnerUserService
 * @date: 2025/10/8 15:41
 * @description: 内部用户服务
 */
public interface InnerUserService {

    /**
     * 根据 id 列表查询用户列表
     *
     * @param ids id 列表
     * @return 用户列表
     */
    List<User> listByIds(Collection<? extends Serializable> ids);

    /**
     * 根据 id 获取用户
     *
     * @param id id
     * @return 用户
     */
    User getById(Serializable id);

    /**
     * 获取当前登录用户
     * @param user 当前用户
     * @return 用户视图对象
     */
    UserVO getUserVO(User user);

    /**
     * 获取当前登录用户
     * @param request HTTP请求对象
     * @return  用户
     */
    static User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
}
