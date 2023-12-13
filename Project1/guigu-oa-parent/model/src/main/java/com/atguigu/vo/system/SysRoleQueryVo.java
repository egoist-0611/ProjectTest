package com.atguigu.vo.system;

import lombok.Data;

/**
 * 封装查询条件的实体类：角色查询条件
 */
@Data
public class SysRoleQueryVo {
    /**
     * 根据角色名查询
     */
    private String roleName;
}
