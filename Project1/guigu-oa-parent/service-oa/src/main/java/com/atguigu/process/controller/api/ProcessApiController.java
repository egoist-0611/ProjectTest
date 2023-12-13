package com.atguigu.process.controller.api;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.process.OaProcessType;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.atguigu.vo.process.ApprovalVo;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("审批流管理")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin        // 跨域，整合微信端时，前端并未进行跨域处理
public class ProcessApiController {
    @Autowired
    private OaProcessTypeService processTypeService;
    @Autowired
    private OaProcessTemplateService processTemplateService;
    @Autowired
    private OaProcessService processService;
    @Autowired
    private SysUserService userService;

    @ApiOperation("获取所有审批模板分类及模板")
    @GetMapping("/findProcessType")
    public Result<List<OaProcessType>> findProcessTypeAndTemplate() {
        List<OaProcessType> res = processTypeService.findProcessTypeAndTemplate();
        return Result.ok(res);
    }

    @ApiOperation("根据id获取审批模板")
    @GetMapping("/getProcessTemplate/{processTemplateId}")
    public Result<OaProcessTemplate> getProcessTemplateById(@ApiParam("审批模板id") @PathVariable Long processTemplateId) {
        OaProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @ApiOperation("提交审批（创建启动流程）")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo vo) {
        processService.startUp(vo);
        return Result.ok();
    }

    @ApiOperation("待处理审批")
    @GetMapping("/findPending/{page}/{limit}")
    public Result<IPage<ProcessVo>> findPending(@ApiParam("当前页码") @PathVariable Integer page,
                                                @ApiParam("每页显示条目数") @PathVariable Integer limit) {
        IPage<ProcessVo> pageRes = processService.findPending(page, limit);
        return Result.ok(pageRes);
    }

    @ApiOperation("查看审批详情")
    @GetMapping("/show/{id}")
    public Result<Map<String, Object>> show(@ApiParam("流程实例id") @PathVariable Long id) {
        Map<String, Object> map = processService.show(id);
        return Result.ok(map);
    }

    @ApiOperation("进行审批")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo vo) {
        processService.approve(vo);
        return Result.ok();
    }

    @ApiOperation("已处理查看")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result<IPage<ProcessVo>> findProcessed(@ApiParam("当前页码") @PathVariable Integer page,
                                                  @ApiParam("每页显示条目数") @PathVariable Integer limit) {
        IPage<ProcessVo> pageRes = processService.findProcessed(page, limit);
        return Result.ok(pageRes);
    }

    @ApiOperation("已发起查看")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@ApiParam("当前页码") @PathVariable Integer page,
                              @ApiParam("每页显示条目数") @PathVariable Integer limit) {
        IPage<ProcessVo> pageRes = processService.findStarted(page, limit);
        return Result.ok(pageRes);
    }

    @ApiOperation("我的查看")
    @GetMapping("/getCurrentUser")
    public Result<Map<String,Object>> getCurrentUser(){
        Map<String,Object> map = userService.getCurrentUser();
        return Result.ok(map);
    }
}
