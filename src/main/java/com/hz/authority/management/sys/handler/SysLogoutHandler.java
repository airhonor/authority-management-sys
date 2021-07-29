package com.hz.authority.management.sys.handler;

import com.hz.authority.management.sys.constant.Constant;
import com.hz.authority.management.sys.constant.ResultCode;
import com.hz.authority.management.sys.exception.PermissionDeniedException;
import com.hz.authority.management.sys.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-29 10:46
 **/
@Configuration
@Slf4j
public class SysLogoutHandler implements LogoutHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //是用户登出具体逻辑的实现,可以记录用户下线的时间，ip，以下为删除token的逻辑
        String token = request.getHeader(Constant.TOKEN_HEADER).substring(Constant.TOKEN_STARTER.length());
        String username = jwtTokenUtil.getUsername(token);
        if (username == null) {
            throw new PermissionDeniedException(ResultCode.UN_AUTHORIZED);
        }
        jwtTokenUtil.deleteToken(username);

    }
}
