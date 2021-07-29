package com.hz.authority.management.sys.vo;

import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-25 22:56
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserToken {

    private String token;

    private SysUserDetails sysUserDetails;
}
