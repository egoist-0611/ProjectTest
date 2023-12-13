package com.atguigu.security.custom;

import com.atguigu.model.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义UserDetails认证对象
 */
public class MyUserDetails extends User {

    private SysUser user;

    /**
     * 通过传入SysUser，后期可直接通过调用get方法获取到该对象，从而获取到id值
     */
    public MyUserDetails(SysUser user,
                         Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }
}
