package com.atguigu.vo.process;

import lombok.Data;

/**
 * 封装审批流的查询条件
 */
@Data
public class ProcessQueryVo {
    /**
     * 查询关键字
     */
    private String keyword;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 审批模板id
     */
    private Long processTemplateId;

    /**
     * 审批模板类型id
     */
    private Long processTypeId;

    /**
     * 起始时间
     */
    private String createTimeBegin;

    /**
     * 结束时间
     */
    private String createTimeEnd;

    /**
     * 状态（0：默认 1：审批中 2：审批通过 -1：驳回）
     */
    private Integer status;
}
