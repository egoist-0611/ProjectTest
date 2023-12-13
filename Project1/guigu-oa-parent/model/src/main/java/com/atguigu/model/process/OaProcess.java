package com.atguigu.model.process;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批类型
 */
@Data
public class OaProcess extends BaseEntity {
    /**
     * 审批码
     */
    @TableField("process_code")
    private String processCode;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 审批模板id
     */
    @TableField("process_template_id")
    private Long processTemplateId;

    /**
     * 审批模板类型id
     */
    @TableField("process_type_id")
    private Long processTypeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 表单值
     */
    @TableField("form_values")
    private String formValues;

    /**
     * 流程实例id
     */
    @TableField("process_instance_id")
    private String processInstanceId;

    /**
     * 当前审批人
     */
    @TableField("current_auditor")
    private String currentAuditor;

    /**
     * 状态码（0：默认 1：审批中 2：审批通过 -1：驳回）
     */
    private Integer status;
}
