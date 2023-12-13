package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssignRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysUserRoleService extends IService<SysUserRole> {
    /**
     * 根据用户的id，获取用户所拥有的角色，以及角色表中所有的角色对象
     *
     * @param id 用户id
     * @return Map，内含 用户所有的角色、角色表中所有的角色
     */
    Map<String, List<SysRole>> getAssign(Long id);

    /**
     * 设置用户的角色信息
     * @param vo    AssignRoleVo对象，内部包含要修改的用户id及修改的角色id
     */
    void setAssign(AssignRoleVo vo);
}
