package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleMenuService;
import com.common.exception.ExecFailException;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.auth.util.MenuHelper;
import com.atguigu.vo.system.AssignMenuVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;

    @Override
    public List<SysMenu> selectWithNodes() {
        List<SysMenu> sysMenus = baseMapper.selectList(null);
        return MenuHelper.buildTree(sysMenus);
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        // 存在parent_id=id的，则说明该id菜单还有子层菜单
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ExecFailException("删除菜单失败");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> getMenuAndSelect(Long roleId) {
        // 1.获取所有的菜单
        LambdaQueryWrapper<SysMenu> menuWrapper = new LambdaQueryWrapper<>();
        // 获取的菜单有个前提：状态正常可用（status=1）
        menuWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> allMenus = baseMapper.selectList(menuWrapper);
        // 2.获取角色拥有的所有菜单的id（查询角色-菜单关系表）
        LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(SysRoleMenu::getRoleId, roleId).select(SysRoleMenu::getMenuId);
        List<SysRoleMenu> tmp = roleMenuService.list(roleMenuWrapper);
        ArrayList<Long> menuIdList = new ArrayList<>();
        for (SysRoleMenu menu : tmp) {
            menuIdList.add(menu.getMenuId());
        }
        // 3.获取用户所有的 菜单id的详细信息
        for (SysMenu menu : allMenus) {
            if (menuIdList.contains(menu.getId())) {
                menu.setSelect(true);       // 让角色默认选中该菜单
            }
        }
        return MenuHelper.buildTree(allMenus);      // 返回所有的菜单，根据isSelect是否被选中来确定角色默认拥有的菜单
    }

    @Override
    public void setAssign(AssignMenuVo vo) {
        Long roleId = vo.getRoleId();
        // 1.删除该角色之前分配的所有菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuService.remove(wrapper);
        // 2.为角色分配新的菜单
        for (Long menuId : vo.getMenuIdList()) {
            roleMenuService.save(new SysRoleMenu(roleId, menuId));
        }
    }

    @Override
    public List<SysMenu> getMenusByRoles(List<SysRole> userRoles) {
        // 一个用户可能有多个角色，每个角色管理的菜单可能是相同的，因此，我们要去除所有 管理相同 的菜单
        Set<SysMenu> menus = new HashSet<>();
        for (SysRole role : userRoles) {
            // 若角色ID为1，则默认为超级管理员，拥有所有的菜单权限
            if (role.getId() == 1) {
                menus.addAll(baseMapper.selectList(null));
                break;
            } else {
                menus.addAll(baseMapper.getMenuByRoleId(role.getId()));     // 若是相同的菜单，会被Set过滤掉
            }
        }
        return MenuHelper.buildTree(new ArrayList<>(menus));         // 构建层级关系
    }

    @Override
    public List<RouterVo> getRouter(List<SysMenu> menus) {
        return MenuHelper.buildRouter(menus);
    }
}
