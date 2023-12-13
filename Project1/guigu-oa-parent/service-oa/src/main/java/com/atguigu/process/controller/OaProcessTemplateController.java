package com.atguigu.process.controller;

import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.process.OaProcessType;
import com.atguigu.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.exception.FileHandlerException;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.api.process.model.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "审批模版")
@RestController
@RequestMapping("/admin/process/processTemplate")
public class OaProcessTemplateController {
    @Autowired
    private OaProcessTemplateService service;

    @ApiOperation("查询所有审批模板并分类")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<OaProcessTemplate>> index(@ApiParam("当前页码") @PathVariable Integer page, @ApiParam("每页显示页数") @PathVariable Integer limit) {
        Page<OaProcessTemplate> pageObj = new Page<>(page, limit);
        IPage<OaProcessTemplate> pageRes = service.selectAndPage(pageObj);
        return Result.ok(pageRes);
    }

    @ApiOperation("流程定义上传")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        // 获取classpath类路径
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        // 在类路径下创建process文件夹，用于存放上传的流程定义
        File templateFile = new File(path + "/process/");
        if (!templateFile.exists()) {
            templateFile.mkdirs();
        }
        // 获取上传的文件的名
        String filename = file.getOriginalFilename();
        // 创建新的空文件，将上传文件内容存储到文件中
        File zipFile = new File(path + "/process/" + filename);
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
            throw new FileHandlerException(e.getMessage());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("processDefinitionPath", "process/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));
        return Result.ok(map);
    }

    @ApiOperation("新增模板")
    @PostMapping("/save")
    public Result save(@RequestBody OaProcessTemplate processTemplate) {
        boolean res = service.save(processTemplate);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("修改模板")
    @PutMapping("/update")
    public Result update(@RequestBody OaProcessTemplate processTemplate) {
        boolean res = service.updateById(processTemplate);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/remove/{id}")
    public Result delete(@ApiParam("模板id") @PathVariable Long id) {
        boolean res = service.removeById(id);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("获取指定模板")
    @GetMapping("/get/{id}")
    public Result<OaProcessTemplate> getById(@ApiParam("模板id") @PathVariable Long id) {
        OaProcessTemplate processTemplate = service.getById(id);
        return Result.ok(processTemplate);
    }


    @ApiOperation("发布审批模板")
    @GetMapping("/public/{id}")
    public Result publish(@PathVariable Long id) {
        service.publish(id);
        return Result.ok();
    }
}
