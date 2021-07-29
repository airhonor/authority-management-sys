package com.hz.authority.management.sys.service;

import com.hz.authority.management.sys.entity.LoginUserEntity;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-26 10:19
 **/
public interface LoginService {

    /**
     * 获取登录token
     *
     * @param loginUser 用户名密码
     * @return token
     */
    String loginSys(LoginUserEntity loginUser);
}
