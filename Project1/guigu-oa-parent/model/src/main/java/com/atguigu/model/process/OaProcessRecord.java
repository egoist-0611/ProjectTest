package com.atguigu.model.process;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记录审批记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("oa_process_record")
public class OaProcessRecord extends BaseEntity {
    /**
     * 审批流程实例信息的id
     */
    @TableField("process_id")
    private Long processId;

    /**
     * 审批记录描述
     */
    private String description;

    /**
     * 状态码（0：默认 1：审批中 2：审批通过 -1：驳回）
     */
    private Integer status;

    /**
     * 审批用户的id
     */
    @TableField("operate_user_id")
    private Long operateUserId;

    /**
     * 审批用户的用户名
     */
    @TableField("operate_user")
    private String operateUser;
}
