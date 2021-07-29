package com.hz.authority.management.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 角色类
 *
 * @author lihaodong
 * @since 2019-03-14
 */
@Data
@Builder
@TableName("role")
public class Role {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;


}
