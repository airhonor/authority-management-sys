package com.hz.authority.management.sys.service.impl;

import com.hz.authority.management.sys.entity.LoginUserEntity;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import com.hz.authority.management.sys.exception.PermissionDeniedException;
import com.hz.authority.management.sys.utils.JwtTokenUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@RunWith(PowerMockRunner.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;


    @Test
    public void loginSysSuccessTest() throws Exception {
        String token = "this is token";
        LoginUserEntity loginUserEntity = LoginUserEntity.builder()
                .username("admin")
                .password("admin")
                .build();
        SysUserDetails sysUserDetails = new SysUserDetails();
        sysUserDetails.setId(1);
        sysUserDetails.setUsername("admin");
        sysUserDetails.setPassword("admin");
        sysUserDetails.setRoles(null);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserEntity.getUsername(), loginUserEntity.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(sysUserDetails, "admin"));
        PowerMockito.when(loginService, "authenticate", loginUserEntity.getUsername(), loginUserEntity.getPassword())
                .thenReturn(new UsernamePasswordAuthenticationToken(sysUserDetails, "admin"));
        Mockito.when(jwtTokenUtil.generateAccessToken(sysUserDetails)).thenReturn(token);
        String actualToken = loginService.loginSys(loginUserEntity);
        Assert.assertEquals(token, actualToken);
    }

    @Test(expected = PermissionDeniedException.class)
    public void loginSysFailedTest() throws Exception {
        String token = "this is token";
        LoginUserEntity loginUserEntity = LoginUserEntity.builder()
                .username("admin")
                .password("admin")
                .build();
        SysUserDetails sysUserDetails = new SysUserDetails();
        sysUserDetails.setId(1);
        sysUserDetails.setUsername("admin");
        sysUserDetails.setPassword("admin");
        sysUserDetails.setRoles(null);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserEntity.getUsername(), loginUserEntity.getPassword())))
                .thenThrow(new BadCredentialsException("bad credential"));
        loginService.loginSys(loginUserEntity);
    }

}