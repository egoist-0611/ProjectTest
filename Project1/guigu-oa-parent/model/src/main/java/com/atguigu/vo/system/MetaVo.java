package com.atguigu.vo.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端路由的其他配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaVo {
    /**
     * 路由名称
     */
    private String title;

    /**
     * 路由图标
     */
    private String icon;
}
