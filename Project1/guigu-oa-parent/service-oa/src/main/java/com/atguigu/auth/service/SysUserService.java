package com.atguigu.auth.service;

import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    /**
     * 修改用户状态
     *
     * @param id     用户id
     * @param status 状态：1可用 0停用
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据用户id，获取用户信息
     *
     * @param userId 用户id
     * @return Map，内含用户名、用户角色、用户菜单按钮操作权限、路由信息
     */
    Map<String, Object> getUserInfo(Long userId);

    /**
     * 根据用户id获取用户的权限
     *
     * @param userId 用户id
     * @return 用户的所有权限（String集合）
     */
    List<String> getUserPermsByUserId(Long userId);

    /**
     * 我的信息查看
     * @return Map，封装了用户名、手机号
     */
    Map<String, Object> getCurrentUser();
}
