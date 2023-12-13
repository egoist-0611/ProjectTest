package com.atguigu.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.OaProcess;
import com.atguigu.model.process.OaProcessRecord;
import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.system.SysUser;
import com.atguigu.process.mapper.OaProcessMapper;
import com.atguigu.process.service.OaProcessRecordService;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.vo.process.ApprovalVo;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.atguigu.wechat.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.LoginUserInfoHelper;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, OaProcess> implements OaProcessService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OaProcessTemplateService processTemplateService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private OaProcessRecordService processRecordService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MessageService messageService;

    @Override
    public IPage<ProcessVo> selectAndPage(Page<ProcessVo> pageObj, ProcessQueryVo vo) {
        return baseMapper.selectAndPage(pageObj, vo);
    }

    @Override
    public void deployProcess(String zipPath) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(zipPath);
        ZipInputStream zipIs = new ZipInputStream(is);
        repositoryService.createDeployment().addZipInputStream(zipIs).deploy();
    }

    @Override
    public void startUp(ProcessFormVo vo) {
        // 1.1.添加审批流程实例信息
        // 获取登录的用户信息
        SysUser user = userService.getById(LoginUserInfoHelper.getUserId());
        // 获取使用的审批模板信息
        OaProcessTemplate processTemplate = processTemplateService.getById(vo.getProcessTemplateId());
        // 添加流程实例记录
        OaProcess process = new OaProcess();
        process.setProcessCode(System.currentTimeMillis() + "");
        process.setUserId(user.getId());
        process.setProcessTemplateId(vo.getProcessTemplateId());
        process.setProcessTypeId(vo.getProcessTypeId());
        process.setTitle(user.getName() + "发起" + processTemplate.getName() + "申请");
        process.setFormValues(vo.getFormValues());
        process.setStatus(1);
        baseMapper.insert(process);
        // 2.创建启动流程实例
        // 获取流程定义key
        String processKey = processTemplate.getProcessDefinitionKey();
        // 将新增的提交记录id，作为创建流程时的业务id
        String businessKey = process.getId() + "";
        // 获取表单中的数据JSON字符串，封装进一个Map后，添加进流程实例的创建中
        JSONObject jsonObj = JSON.parseObject(vo.getFormValues());
        JSONObject formData = jsonObj.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> data : formData.entrySet()) {
            map.put(data.getKey(), data.getValue());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", map);
        // 创建启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, data);
        // 查询下一个审批人的信息（若是并行任务，则可能有多个审批人）
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        ArrayList<SysUser> assignees = new ArrayList<>();
        for (Task task : tasks) {
            SysUser assignee = userService.getUserByUsername(task.getAssignee());
            assignees.add(assignee);
            // 3.通知审批人处理审批
            messageService.pushPendingMessage(process.getId(), assignee.getId(), task.getId());
        }
        // 1.2.完善新添加的流程实例创建记录
        process.setDescription("等待" + assignees + "审批");
        process.setProcessInstanceId(processInstance.getId());
        baseMapper.updateById(process);
        // 4.记录审批记录
        processRecordService.save(new OaProcessRecord(process.getId(), "发起申请", 1, user.getId(), user.getName()));
    }

    @Override
    public IPage<ProcessVo> findPending(Integer page, Integer limit) {
        // 获取当前用户（审批人）的待审批任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername());
        // 获取任务并分页（参数一：从哪条数据开始显示 参数二：显示的条目数）
        List<Task> tasks = taskQuery.listPage((page - 1) * limit, limit);
        // 将已分页了的数据重新提取，再封装进List<ProcessVo>中
        // tasks中没有ProcessVo的数据，但是可以通过获取tasks中记录的流程实例id，进而获取到流程实例中的绑定的业务id，从而获取到OaProcess中封装的数据
        List<ProcessVo> pageList = new ArrayList<>();
        for (Task task : tasks) {
            // 根据流程实例id获取流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            // 获取业务id，进而获取到Process
            Long processId = Long.valueOf(processInstance.getBusinessKey());
            OaProcess process = baseMapper.selectById(processId);
            ProcessVo processVo = new ProcessVo();
            // 将Process中，与ProcessVo共有的属性进行值的复制
            BeanUtils.copyProperties(process, processVo);
            // 完善ProcessVo
            processVo.setTaskId(task.getId());
            pageList.add(processVo);
        }
        // 创建一个新的分页对象，内需三个属性：当前页码、每页显示条目数、总条目数，添加分页后的数据，最后返回IPage对象
        IPage<ProcessVo> pageObj = new Page<>(page, limit, taskQuery.count());
        pageObj.setRecords(pageList);
        return pageObj;
    }

    @Override
    public Map<String, Object> show(Long id) {
        // 查询审批流程实例信息
        OaProcess process = baseMapper.selectById(id);
        // 根据审批流程实例的id，查看该审批的审批记录
        LambdaQueryWrapper<OaProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OaProcessRecord::getProcessId, id);
        List<OaProcessRecord> processRecordList = processRecordService.list(wrapper);
        // 查看审批模板
        OaProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        // 查看当前用户是否有权限进行审批
        boolean isApprove = false;
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getProcessInstanceId()).list();
        String username = LoginUserInfoHelper.getUsername();
        for (Task task : tasks) {
            String assignee = task.getAssignee();
            if (username.equals(assignee)) {
                isApprove = true;
            }
        }
        // 返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    @Override
    public void approve(ApprovalVo vo) {
        OaProcess process = baseMapper.selectById(vo.getProcessId());
        OaProcessRecord processRecord = new OaProcessRecord();
        if (vo.getStatus() == 1) {  // 通过审批
            // 完成审批任务
            String taskId = vo.getTaskId();
            taskService.complete(taskId);
        } else {    // 不通过审批
            // 结束审批流程实例（通过 让流程任务完成后 直接到结束事件节点 来实现）
            // 根据流程定义id获取BpmnModel
            Task task = taskService.createTaskQuery().taskId(vo.getTaskId()).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            // 获取流程的结束事件节点
            List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
            if (!CollectionUtils.isEmpty(endEventList)) {   // 若是并行流程任务的话，则可能没有结束事件节点
                FlowNode endFlowNode = endEventList.get(0);
                // 获取当前流程的任务节点
                FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
                // 删除当前任务节点后续的走向
                currentFlowNode.getOutgoingFlows().clear();
                // 创建新的走向
                SequenceFlow newSequenceFlow = new SequenceFlow();
                newSequenceFlow.setId("newSequenceFlowId");     // 设置走向的id
                newSequenceFlow.setSourceFlowElement(currentFlowNode);      // 设置走向的起始节点
                newSequenceFlow.setTargetFlowElement(endFlowNode);      // 设置走向的最终节点
                // 设置当前流程的任务节点之后的新走向
                List<SequenceFlow> list = new ArrayList<>();
                list.add(newSequenceFlow);
                currentFlowNode.setOutgoingFlows(list);
                // 完成任务后，流程将走向结束事件节点，从而结束流程
                taskService.complete(task.getId());
            }
        }
        // 获取下一个审批人（可能要多个人都通过审批），并 TODO 推送消息
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getProcessInstanceId()).list();
        if (!CollectionUtils.isEmpty(tasks)) {
            List<String> assignees = new ArrayList<>();
            for (Task task : tasks) {
                SysUser user = userService.getUserByUsername(task.getAssignee());
                assignees.add(user.getName());
            }
            process.setDescription("等待" + assignees + "审批");
        } else {    // 若没有审批任务，则说明已不需要再审批
            if (vo.getStatus() == 1) {
                process.setDescription("审批完成：通过");
                process.setStatus(2);
                processRecord.setStatus(2);
            } else {
                process.setDescription("审批完成：驳回");
                process.setStatus(-1);
                processRecord.setStatus(-1);
            }
        }
        // 更新审批流程信息
        baseMapper.updateById(process);
        // 添加审批记录
        SysUser user = userService.getById(LoginUserInfoHelper.getUserId());
        String description = user.getName() + (vo.getStatus() == 1 ? "通过审批" : "驳回审批");
        processRecord.setProcessId(process.getId());
        processRecord.setDescription(description);
        processRecord.setOperateUserId(user.getId());
        processRecord.setOperateUser(user.getName());
        processRecordService.save(processRecord);
    }

    @Override
    public IPage<ProcessVo> findProcessed(Integer page, Integer limit) {
        // 查看用户所有已处理完成的审批任务
        HistoricTaskInstanceQuery finishedQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(LoginUserInfoHelper.getUsername()).finished();
        // 分页获取
        List<HistoricTaskInstance> pageHistoryTask = finishedQuery.listPage((page - 1) * limit, limit);
        // 获取总的数据量（未分页）
        long count = finishedQuery.count();
        // 遍历HistoricTaskInstance，获取到processInstanceId，从而获取到Process，进而封装为ProcessVo
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance taskInstance : pageHistoryTask) {
            LambdaQueryWrapper<OaProcess> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OaProcess::getProcessInstanceId, taskInstance.getProcessInstanceId());
            OaProcess process = baseMapper.selectOne(wrapper);
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVoList.add(processVo);
        }
        // 封装为IPage并返回
        IPage<ProcessVo> pageObj = new Page<>(page, limit, count);
        pageObj.setRecords(processVoList);
        return pageObj;
    }

    @Override
    public IPage<ProcessVo> findStarted(Integer page, Integer limit) {
        Page<ProcessVo> pageObj = new Page<>(page, limit);
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        return baseMapper.selectAndPage(pageObj, processQueryVo);
    }
}
