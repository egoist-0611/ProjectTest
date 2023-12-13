package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssignMenuVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 有层级的显示菜单
     *
     * @return SysMenu菜单集合，SysMenu中有一个children，会存放其子层的菜单
     */
    List<SysMenu> selectWithNodes();

    /**
     * 通过菜单id删除菜单信息（有子层菜单的不能进行删除）
     *
     * @param id 菜单id
     */
    void removeMenuById(Long id);

    /**
     * 根据所有的菜单信息，标记指定角色id拥有的菜单信息（isSelect=true)
     *
     * @param roleId 角色id
     * @return 分级好的所有的菜单信息
     */
    List<SysMenu> getMenuAndSelect(Long roleId);

    /**
     * 为指定角色设置菜单
     *
     * @param vo 内含要修改的角色的id、要赋予该角色的菜单的id
     */
    void setAssign(AssignMenuVo vo);

    /**
     * 通过角色id，获取每个角色id对应的菜单信息
     *
     * @param userRoles list，含多个角色id
     * @return 指定角色id的所有菜单信息
     */
    List<SysMenu> getMenusByRoles(List<SysRole> userRoles);

    /**
     * 根据菜单信息，构建路由信息
     * @param menus 菜单
     * @return  RouterVo，内含前端路由所需要的信息
     */
    List<RouterVo> getRouter(List<SysMenu> menus);
}
