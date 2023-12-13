package com.atguigu.vo.system;

import lombok.Data;

/**
 * 用户查询条件封装
 */
@Data
public class SysUserQueryVo {
    /**
     * 查询的关键字：可能为 用户名/姓名/手机号码 需要匹配
     */
    private String keyword;

    /**
     * 任职时间点大于等于该时间
     */
    private String createTimeBegin;

    /**
     * 任职时间点小于等于该时间
     */
    private String createTimeEnd;
}
