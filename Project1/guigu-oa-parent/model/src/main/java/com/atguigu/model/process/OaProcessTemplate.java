package com.atguigu.model.process;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 审批模板
 */
@Data
public class OaProcessTemplate extends BaseEntity {
    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板图标路径
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 审批类型id
     */
    @TableField("process_type_id")
    private Long processTypeId;

    /**
     * 审批类型名称
     */
    @TableField(exist = false)
    private String processTypeName;

    /**
     * 表单属性
     */
    @TableField("form_props")
    private String formProps;

    /**
     * 表单选项
     */
    @TableField("form_options")
    private String formOptions;

    /**
     * 流程定义key
     */
    @TableField("process_definition_key")
    private String processDefinitionKey;

    /**
     * 流程定义上传路径
     */
    @TableField("process_definition_path")
    private String processDefinitionPath;

    /**
     * 流程定义模型id
     */
    @TableField("process_model_id")
    private String processModelId;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private Integer status;

}
