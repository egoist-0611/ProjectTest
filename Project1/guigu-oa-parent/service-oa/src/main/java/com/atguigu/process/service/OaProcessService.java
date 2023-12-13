package com.atguigu.process.service;

import com.atguigu.model.process.OaProcess;
import com.atguigu.vo.process.ApprovalVo;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface OaProcessService extends IService<OaProcess> {
    /**
     * 根据条件进行查询并分页
     *
     * @param pageObj Page对象
     * @param vo      内部封装了查询条件
     * @return IPage分页后的数据
     */
    IPage<ProcessVo> selectAndPage(Page<ProcessVo> pageObj, ProcessQueryVo vo);

    /**
     * 部署流程定义
     *
     * @param zipPath zip文件路径
     */
    void deployProcess(String zipPath);

    /**
     * 提交审批（创建和启动流程）
     *
     * @param vo 封装了启动流程时所需要的内容
     */
    void startUp(ProcessFormVo vo);

    /**
     * 获取当前用户要审批的内容，并进行分页
     *
     * @param page  当前页码
     * @param limit 每页显示条目数
     * @return IPage分页后的数据
     */
    IPage<ProcessVo> findPending(Integer page, Integer limit);

    /**
     * 根据审批流程实例的id，获取该审批的详细信息
     *
     * @param id 审批流程实例id
     * @return Map，封装了：审批模板信息、审批流程记录、审批流程实例信息、是否有权限处理审批
     */
    Map<String, Object> show(Long id);

    /**
     * 进行审批
     *
     * @param vo 封装了进行审批时所需要的参数
     */
    void approve(ApprovalVo vo);

    /**
     * 查看当前登录的用户所有已处理的审批任务（包括审批通过或驳回的）并分页显示
     *
     * @param page  当前页码
     * @param limit 每页显示条目数
     * @return 分页后的数据
     */
    IPage<ProcessVo> findProcessed(Integer page, Integer limit);

    /**
     * 查看当前登录的用户所有发起的审批任务
     *
     * @param page  当前页码
     * @param limit 每页显示条目数
     * @return 分页后的数据
     */
    IPage<ProcessVo> findStarted(Integer page, Integer limit);
}
