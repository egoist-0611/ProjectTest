package com.atguigu.vo.wechat;

import lombok.Data;

/**
 * 封装微信账号绑定手机号时需要的信息
 */
@Data
public class BindPhoneVo {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 要设置给用户的openId
     */
    private String openId;
}
