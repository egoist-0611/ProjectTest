package com.atguigu.process.service.impl;

import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.process.OaProcessType;
import com.atguigu.process.mapper.OaProcessTemplateMapper;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OaProcessTemplateServiceTemplateImpl extends ServiceImpl<OaProcessTemplateMapper, OaProcessTemplate> implements OaProcessTemplateService {
    @Autowired
    private OaProcessTypeService processTypeService;
    @Autowired
    private OaProcessService processService;

    @Override
    public IPage<OaProcessTemplate> selectAndPage(Page<OaProcessTemplate> pageObj) {
        Page<OaProcessTemplate> pageRes = baseMapper.selectPage(pageObj, null);
        List<OaProcessTemplate> templates = pageRes.getRecords();
        for (OaProcessTemplate template : templates) {
            // 获取审批类型id，根据id查询审批类型名称，进行显示
            Long processTypeId = template.getProcessTypeId();
            OaProcessType processType = processTypeService.getById(processTypeId);
            template.setProcessTypeName(processType.getName());
        }
        return pageRes;
    }

    @Override
    public void publish(Long id) {
        // 1.修改模板状态（1表示已发布，0表示未发布）
        OaProcessTemplate template = baseMapper.selectById(id);
        template.setStatus(1);
        baseMapper.updateById(template);
        // 2.部署模板定义
        String zipPath = template.getProcessDefinitionPath();
        if (!StringUtils.isEmpty(zipPath)) {
            processService.deployProcess(zipPath);
        }
    }
}
