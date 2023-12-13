package com.atguigu.auth.mapper;

import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 联合多张表，根据用户id查询用户的权限
     *
     * @param userId 用户id
     * @return 用户所有权限（String集合）
     */
    List<String> getUserPermsByUserId(Long userId);
}
