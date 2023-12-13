package com.atguigu.vo.process;

import lombok.Data;

/**
 * 封装进行审批时所需要的数据
 */
@Data
public class ApprovalVo {
    /**
     * 审批流程实例信息id
     */
    private Long processId;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 状态（由前端传入，1表示要通过审批）
     */
    private Integer status;

    /**
     * 审批描述
     */
    private String description;
}
