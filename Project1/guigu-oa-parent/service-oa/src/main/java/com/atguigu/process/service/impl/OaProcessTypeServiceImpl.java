package com.atguigu.process.service.impl;

import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.process.OaProcessType;
import com.atguigu.process.mapper.OaProcessTypeMapper;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, OaProcessType> implements OaProcessTypeService {
    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Override
    public List<OaProcessType> findProcessTypeAndTemplate() {
        // 获取所有审批模板类型
        List<OaProcessType> processTypes = baseMapper.selectList(null);
        for (OaProcessType processType : processTypes) {
            // 获取指定审批模板类型的审批模板
            LambdaQueryWrapper<OaProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OaProcessTemplate::getProcessTypeId, processType.getId());
            List<OaProcessTemplate> templates = processTemplateService.list(wrapper);
            processType.setTemplateList(templates);
        }
        return processTypes;
    }
}
