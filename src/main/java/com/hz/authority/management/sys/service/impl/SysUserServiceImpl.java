package com.hz.authority.management.sys.service.impl;

import com.hz.authority.management.sys.entity.Role;
import com.hz.authority.management.sys.entity.User;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import com.hz.authority.management.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @program: authority-managment-sys
 * @author: zgr
 * @create: 2021-07-25 20:48
 **/

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("根据用户名{}查询用户信息", username);
        //此处实际可以从数据库中查找对应的用户名
        User user = User.builder()
                .id(1)
                .username("admin")
                .password("$2a$10$blrIf6.vDYUAGbq.8fk2heScZYVgMl8lFAUWvPi1aZ9aiCar3pALe")
                .test("this is test")
                .build();
        Role role = Role.builder()
                .id(1)
                .name("admin")
                .build();


        //这里权限列表,这个为方便直接下（实际开发中查询用户时连表查询出权限）
        return new SysUserDetails(user.getId(), user.getUsername(), user.getPassword(), role);
    }
}
