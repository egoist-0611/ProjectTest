package com.atguigu.auth.service.impl;

import com.atguigu.auth.service.SysUserService;
import com.common.exception.AuthenticationFailException;
import com.atguigu.model.system.SysUser;
import com.atguigu.security.custom.MyUserDetails;
import com.atguigu.security.custom.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
    @Autowired
    private SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // 根据用户名查询数据库用户信息
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
            throw new AuthenticationFailException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new AuthenticationFailException("账号已停用");
        }
        // 获取用户的操作权限，并封装进UserDetails中进行返回。当认证成功时，可以获取到UserDetails中的权限，并保存到缓存中；之后，在进行token认证时，可从缓存中获取权限，进而决定访问的权限
        List<String> userPerms = userService.getUserPermsByUserId(user.getId());
        // 封装进UserDetails中的权限对象，要求类型为GrantedAuthority的集合，SimpleGrantedAuthority是其实现类
        List<SimpleGrantedAuthority> permsList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userPerms)) {
            permsList = Collections.emptyList();
        } else {
            for (String perm : userPerms) {
                permsList.add(new SimpleGrantedAuthority(perm));
            }
        }
        // 返回一个UserDetails（MyUserDetails继承于User，而User又是UserDetails的实现类），用于与表单提交的用户信息做对比认证
        return new MyUserDetails(user, permsList);
    }
}
