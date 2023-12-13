package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysMenuService menuService;

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = baseMapper.selectById(id);
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getUserInfo(Long userId) {
        Map<String, Object> map = new HashMap<>();
        // 1.获取用户的姓名
        String name = baseMapper.selectById(userId).getName();
        map.put("name", name);
        // 2.获取用户的角色（查询用户-角色表）
        List<SysRole> userRoles = userRoleService.getAssign(userId).get("assignRoleList");
        map.put("roles", userRoles);
        // 根据用户的角色，获取角色对应的菜单
        List<SysMenu> menus = menuService.getMenusByRoles(userRoles);
        // 3.根据用户的菜单，构建路由信息
        List<RouterVo> router = menuService.getRouter(menus);
        map.put("routers", router);
        // 4.根据用户的id，获取用户的操作权限
        List<String> buttons = getUserPermsByUserId(userId);
        map.put("buttons", buttons);
        return map;
    }

    @Override
    public List<String> getUserPermsByUserId(Long userId) {
        // 设定用户id为1为超级管理员，拥有所有操作权限
        if (userId == 1) {
            List<SysMenu> menus = menuService.list(null);
            List<String> adminPerms = new ArrayList<>();
            for (SysMenu menu : menus) {
                String perm = menu.getPerms();
                if (perm != null && !"".equals(perm)) {
                    adminPerms.add(perm);
                }
            }
            return adminPerms;
        }
        return baseMapper.getUserPermsByUserId(userId);
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser user = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("phone", user.getPhone());
        return map;
    }
}
