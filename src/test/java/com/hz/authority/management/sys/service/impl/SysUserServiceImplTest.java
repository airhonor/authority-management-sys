package com.hz.authority.management.sys.service.impl;

import com.hz.authority.management.sys.entity.Role;
import com.hz.authority.management.sys.entity.User;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class SysUserServiceImplTest {

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    @Test
    public void loadUserByUsernameSuccessTest() {

        User user = User.builder()
                .id(1)
                .username("admin")
                .password("$2a$10$blrIf6.vDYUAGbq.8fk2heScZYVgMl8lFAUWvPi1aZ9aiCar3pALe")
                .test("this is test")
                .build();

        Role role1 = Role.builder()
                .id(1)
                .name("READ")
                .build();
        Role role2 = Role.builder()
                .id(1)
                .name("WRITE")
                .build();

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        SysUserDetails sysUserDetails =
                new SysUserDetails(user.getId(), user.getUsername(), user.getPassword(), roles);
        UserDetails actualSysUserDetails = sysUserService.loadUserByUsername(user.getUsername());
        Assert.assertEquals(sysUserDetails, actualSysUserDetails);
    }


    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameFailedTest() {
        String userName = "admin-no";
        PowerMockito.when(sysUserService.loadUserByUsername(userName)).thenThrow(UsernameNotFoundException.class);
    }

}