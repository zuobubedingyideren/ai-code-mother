package com.px.aicodemother.constants;

/**
 * packageName: com.px.aicodemother.constants
 *
 * @author: idpeng
 * @version: 1.0
 * @interfaceName: UserConstant
 * @date: 2025/9/17 23:11
 * @description: 用户常量
 */
public interface UserConstant {

    /**
     * 盐值
     */
    String USER_SATT = "tiantianxiangxhang";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "12345678";

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    // endregion
}
