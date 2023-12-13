package com.atguigu.model.process;

import com.atguigu.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 审批类型
 */
@Data
@TableName("oa_process_type")
public class OaProcessType extends BaseEntity {

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型描述
     */
    private String description;

    /**
     * 该审批模板类型下的所有审批模板
     */
    @TableField(exist = false)
    private List<OaProcessTemplate> templateList;
}
