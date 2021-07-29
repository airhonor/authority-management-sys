package com.hz.authority.management.sys.service.impl;

import com.hz.authority.management.sys.entity.LoginUserEntity;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import com.hz.authority.management.sys.exception.PermissionDeniedException;
import com.hz.authority.management.sys.service.LoginService;
import com.hz.authority.management.sys.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-26 10:20
 **/

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String loginSys(LoginUserEntity loginUser) {

        final Authentication authentication = authenticate(loginUser.getUsername(), loginUser.getPassword());
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        log.info("{} 登录，生成token", loginUser.getUsername());
        final SysUserDetails userDetail = (SysUserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        jwtTokenUtil.putToken(loginUser.getUsername(), token);
        return token;
    }


    private Authentication authenticate(String username, String password) {
        try {
            // 该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，
            // 如果正确，则存储该用户名密码到security 的 context中
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new PermissionDeniedException("用户名或密码错误，请重新登录");
        }
    }
}
