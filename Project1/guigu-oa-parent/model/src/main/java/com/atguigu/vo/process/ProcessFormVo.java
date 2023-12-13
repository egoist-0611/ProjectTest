package com.atguigu.vo.process;

import lombok.Data;

/**
 * 封装 提交审批（创建启动流程实例）时 需要的数据
 */
@Data
public class ProcessFormVo {
    /**
     * 审批模板id
     */
    private Long processTemplateId;

    /**
     * 审批模板类型id
     */
    private Long processTypeId;

    /**
     * 表单内容（JSON字符串）
     */
    private String formValues;

}
