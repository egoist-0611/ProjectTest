package com.atguigu.vo.system;

import lombok.Data;

import java.util.List;

/**
 * 存储要修改的用户-角色关系
 */
@Data
public class AssignRoleVo {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户拥有的角色id
     */
    private List<Long> roleIdList;
}
