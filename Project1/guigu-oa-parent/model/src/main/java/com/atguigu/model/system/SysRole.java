package com.atguigu.model.system;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 */
@Data       // 生成get/set方法
@AllArgsConstructor     // 生成全参构造器
@NoArgsConstructor      // 生成无参构造器
@TableName("sys_role")                // 结果集表与实体类绑定
public class SysRole extends BaseEntity {
    /**
     * 角色名称
     */
    @TableField("role_name")        // 字段与属性互绑
    private String roleName;

    /**
     * 角色编号
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 描述
     */
    private String description;
}
