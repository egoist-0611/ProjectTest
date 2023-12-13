package com.atguigu.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义loadUserByUsername认证过程
 */
public interface MyUserDetailsService extends UserDetailsService {
    /**
     * 重新定义loadUserByUsername，在其内进行数据库查询数据，返回的UserDetails将作为 与表单提交的用户信息 进行比较的数据
     *
     * @param username 用户名
     * @return UserDetails类型，实现类为MyUserDetails（其父类User，间接实现了UserDetails接口）
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
