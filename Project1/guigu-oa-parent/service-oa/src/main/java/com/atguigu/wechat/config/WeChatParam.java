package com.atguigu.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 绑定微信
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")        // 属性与配置文件绑定
public class WeChatParam {
    private String mpAddId;
    private String mpAppSecret;
}
