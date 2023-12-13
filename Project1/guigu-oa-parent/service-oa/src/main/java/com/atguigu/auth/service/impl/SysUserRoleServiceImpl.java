package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssignRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Autowired
    private SysRoleService roleService;

    @Override
    public Map<String, List<SysRole>> getAssign(Long id) {
        Map<String, List<SysRole>> map = new HashMap<>();
        // 1.获取所有的角色
        List<SysRole> allRoleList = roleService.list();
        map.put("allRolesList", allRoleList);
        // 2.获取指定id的用户的角色
        // 获取sys_user_role表中，记录的 指定用户id所有的角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, id);
        List<SysUserRole> tmp = baseMapper.selectList(wrapper);
        List<Long> userRoleId = new ArrayList<>();
        for (SysUserRole userRole : tmp) {
            userRoleId.add(userRole.getRoleId());
        }
        // 获取角色id对应的角色（用户拥有的角色）
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole role : allRoleList) {
            if (userRoleId.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }
        map.put("assignRoleList", assignRoleList);
        return map;
    }

    @Override
    public void setAssign(AssignRoleVo vo) {
        // 1.删除原先sys_user_role表中，该用户拥有的所有角色
        Long userId = vo.getUserId();
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        baseMapper.delete(wrapper);
        // 2.为指定id的用户添加上新角色
        List<Long> roleIdList = vo.getRoleIdList();
        for (Long roleId : roleIdList) {
            SysUserRole userRole = new SysUserRole(roleId, userId);
            baseMapper.insert(userRole);
        }
    }
}
