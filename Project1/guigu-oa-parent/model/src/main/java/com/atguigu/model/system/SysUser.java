package com.atguigu.model.system;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 用户表
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;


    @TableField("head_url")
    private String headUrl;


    @TableField("dept_id")
    private Integer deptId;


    @TableField("post_id")
    private Integer postId;


    @TableField("open_id")
    private String openId;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 用户状态（1：正常 0：停用）
     */
    private Integer status;

    @TableField(exist = false)
    private List<SysRole> roleList;
    //岗位
    @TableField(exist = false)
    private String postName;
    //部门
    @TableField(exist = false)
    private String deptName;
}
