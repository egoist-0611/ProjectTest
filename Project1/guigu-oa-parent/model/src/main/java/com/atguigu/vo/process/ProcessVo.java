package com.atguigu.vo.process;

import com.atguigu.model.process.OaProcess;
import lombok.Data;

import java.util.Date;

/**
 * 封装审批流条件查询后的结果
 */
@Data
public class ProcessVo extends OaProcess {
    private Long id;
    private Date createTime;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 审批模板名称
     */
    private String processTemplateName;

    /**
     * 审批模板类型名称
     */
    private String processTypeName;

    /**
     * 表单属性
     */
    private String formProps;

    /**
     * 表单选项
     */
    private String formOptions;

    private String taskId;
}
