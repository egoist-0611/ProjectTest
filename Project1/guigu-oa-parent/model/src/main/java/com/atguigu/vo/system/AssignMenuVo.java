package com.atguigu.vo.system;

import lombok.Data;

import java.util.List;

/**
 * 存储要修改的角色-菜单关系
 */
@Data
public class AssignMenuVo {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 要赋予角色的菜单的id
     */
    private List<Long> menuIdList;
}
