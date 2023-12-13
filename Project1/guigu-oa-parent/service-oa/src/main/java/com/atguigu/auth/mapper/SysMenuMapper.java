package com.atguigu.auth.mapper;

import com.atguigu.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 通过角色id获取菜单信息
     *
     * @param roleId 角色id
     * @return SysMenu菜单对象
     */
    List<SysMenu> getMenuByRoleId(Long roleId);
}
