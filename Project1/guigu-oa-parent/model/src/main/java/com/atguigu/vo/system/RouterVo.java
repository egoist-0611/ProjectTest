package com.atguigu.vo.system;

import lombok.Data;

import java.util.List;

/**
 * 配置前端路由信息
 */
@Data
public class RouterVo {
    /**
     * 路由基础地址
     */
    private String path;

    /**
     * 该路由是否为隐藏
     */
    private Boolean hidden;

    /**
     * 渲染页面地址
     */
    private String component;

    /**
     * 当有子路由时，是否嵌套（可折叠）
     */
    private Boolean alwaysShow;

    /**
     * 其他配置
     */
    private MetaVo meta;

    /**
     * 子路由
     */
    private List<RouterVo> children;
}
