package com.px.aicodeuser.service.impl;

import com.px.aicodemother.innerservice.InnerUserService;
import com.px.aicodemother.model.entity.User;
import com.px.aicodemother.model.vo.user.UserVO;
import com.px.aicodeuser.service.UserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * packageName: com.px.aicodeuser.service.impl
 *
 * @author: idpeng
 * @version: 1.0
 * @className: InnerUserServiceImpl
 * @date: 2025/10/8 17:48
 * @description: 内部用户服务实现类
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;


    @Override
    public List<User> listByIds(Collection<? extends Serializable> ids) {
        return userService.listByIds(ids);
    }

    @Override
    public User getById(Serializable id) {
        return userService.getById(id);
    }

    @Override
    public UserVO getUserVO(User user) {
        return userService.getUserVO(user);
    }
}
