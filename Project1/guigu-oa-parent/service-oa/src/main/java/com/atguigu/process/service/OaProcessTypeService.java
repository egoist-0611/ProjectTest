package com.atguigu.process.service;

import com.atguigu.model.process.OaProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OaProcessTypeService extends IService<OaProcessType> {
    /**
     * 获取所有审批模板类型及该类型下所有的模板
     *
     * @return OaProcessType集合，内含了该类型的审批模板
     */
    List<OaProcessType> findProcessTypeAndTemplate();
}
