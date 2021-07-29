package com.hz.authority.management.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-25 22:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserEntity {

    /**
     * 姓名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
