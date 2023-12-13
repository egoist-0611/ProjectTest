package com.atguigu.process.controller;

import com.atguigu.model.process.OaProcessType;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "审批类型")
@RestController
@RequestMapping("/admin/process/processType")
public class OaProcessTypeController {
    @Autowired
    private OaProcessTypeService service;

    /**
     * 分页获取审批类型列表
     *
     * @param page  当前页码
     * @param limit 每页显示条目数
     * @return Result统一结果集，内含Page<OaProcessType>分页对象
     */
    @ApiOperation("分页获取")
    @GetMapping("/{page}/{limit}")
    public Result<Page<OaProcessType>> index(@ApiParam("当前页码") @PathVariable Integer page, @ApiParam("每页显示条目数") @PathVariable Integer limit) {
        Page<OaProcessType> pageObj = new Page<>(page, limit);
        Page<OaProcessType> pageRes = service.page(pageObj);
        return Result.ok(pageRes);
    }

    @ApiOperation("根据类型id获取")
    @GetMapping("/get/{id}")
    public Result<OaProcessType> get(@ApiParam("类型id") @PathVariable Long id) {
        OaProcessType processType = service.getById(id);
        return Result.ok(processType);
    }

    @ApiOperation("新增类型")
    @PostMapping("/save")
    public Result save(@RequestBody OaProcessType oaProcessType) {
        boolean res = service.save(oaProcessType);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("修改类型")
    @PutMapping("/update")
    public Result updateById(@RequestBody OaProcessType oaProcessType) {
        boolean res = service.updateById(oaProcessType);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("删除类型")
    @DeleteMapping("/remove/{id}")
    public Result delete(@ApiParam("类型id") @PathVariable Long id) {
        boolean res = service.removeById(id);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("获取全部类型")
    @GetMapping("/findAll")
    public Result<List<OaProcessType>> findAll() {
        List<OaProcessType> list = service.list();
        return Result.ok(list);
    }
}

