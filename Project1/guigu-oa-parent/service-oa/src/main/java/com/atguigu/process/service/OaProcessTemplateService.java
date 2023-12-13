package com.atguigu.process.service;

import com.atguigu.model.process.OaProcessTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OaProcessTemplateService extends IService<OaProcessTemplate> {
    /**
     * 自定义分页（需要获取其他数据，原分页功能不满足）
     *
     * @param pageObj Page对象
     * @return IPage对象
     */
    IPage<OaProcessTemplate> selectAndPage(Page<OaProcessTemplate> pageObj);

    /**
     * 发布审批模板
     * @param id    审批模板id
     */
    void publish(Long id);
}
