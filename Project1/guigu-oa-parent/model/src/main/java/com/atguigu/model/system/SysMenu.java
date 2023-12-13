package com.atguigu.model.system;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 菜单表
 */
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
    /**
     * 当前菜单的上层菜单（0表示无父菜单）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 当前层菜单的类型（0：目录 1：菜单 2：按钮）
     */
    private Integer type;

    /**
     * 前端路由功能所需：路由地址
     */
    private String path;

    /**
     * 前端路由功能所需：渲染页面地址
     */
    private String component;

    /**
     * 2层类型菜单（按钮）特有
     */
    private String perms;

    /**
     * 前端路由功能所需：图标名称
     */
    private String icon;

    @TableField("sort_value")
    private Integer sortValue;

    /**
     * 菜单状态（0：禁用 1：正常）
     */
    private Integer status;


    /**
     * 子层菜单（非表中字段）
     */
    @TableField(exist = false)
    private List<SysMenu> children;

    /**
     * 是否被选中（角色拥有，则说明是默认选中的）（在修改角色的菜单权限时需要）
     */
    @TableField(exist = false)
    private boolean isSelect = false;
}
