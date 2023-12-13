package com.atguigu.model.wechat;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 微信公众号菜单
 */
@Data
@TableName("wechat_menu")
public class Menu extends BaseEntity {
    /**
     * 上级菜单的id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 类型（0表示为一级菜单）
     */
    private String type;

    /**
     * 网页链接（用户点击菜单可打开链接）
     */
    private String url;

    /**
     * 菜单key值（用于消息接口推送）
     */
    @TableField("menu_key")
    private String menuKey;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 下级菜单
     */
    @TableField(exist = false)
    private List<Menu> children;
}
