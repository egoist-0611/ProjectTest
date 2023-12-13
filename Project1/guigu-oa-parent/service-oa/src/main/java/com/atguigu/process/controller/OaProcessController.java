package com.atguigu.process.controller;

import com.atguigu.process.service.OaProcessService;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "审批流管理")
@RestController
@RequestMapping("/admin/process")
public class OaProcessController {
    @Autowired
    private OaProcessService service;

    @ApiOperation("获取数据并分页")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<ProcessVo>> index(@ApiParam("当前页码") @PathVariable Integer page,
                                          @ApiParam("每页显示页数") @PathVariable Integer limit,
                                          ProcessQueryVo vo) {
        Page<ProcessVo> pageObj = new Page<>(page, limit);
        IPage<ProcessVo> pageRes = service.selectAndPage(pageObj, vo);
        return Result.ok(pageRes);
    }
}
