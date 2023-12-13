package com.atguigu.vo.system;

import lombok.Data;

/**
 * 封装登录时接收的参数
 */
@Data
public class LoginVo {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
