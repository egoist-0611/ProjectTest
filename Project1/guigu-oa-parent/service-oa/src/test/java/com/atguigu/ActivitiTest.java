package com.atguigu;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ActivitiTest {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void test8() {
        String processId = "d5590959-8c23-11ee-ab0b-3613e85dfe49";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();
        if (processInstance.isSuspended()) {
            runtimeService.activateProcessInstanceById(processId);
        } else {
            runtimeService.suspendProcessInstanceById(processId);
        }
    }

    @Test
    public void test7() {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("process")
                .singleResult();
        if (definition.isSuspended()) {
            repositoryService.activateProcessDefinitionById(definition.getId(), true, null);
        } else {
            repositoryService.suspendProcessDefinitionById(definition.getId(), true, null);
        }
    }

    @Test
    public void test6() {
//        repositoryService.deleteDeployment("c0cfa66c-8c20-11ee-8835-3613e85dfe49",true);
        repositoryService.deleteDeployment("60e830b2-8b78-11ee-b89e-3613e85dfe49", true);
    }

    @Test
    public void test5() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("zhangsan")
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getProcessInstanceId());
            System.out.println(historicTaskInstance.getId());
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getName());
        }
    }

    @Test
    public void test4() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }

    @Test
    public void test3() {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .list();
        for (Task task : list) {
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    @Test
    public void test2() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void test1() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/ask_for_leave.bpmn20.xml")
                .addClasspathResource("process/ask_for_leave.png")
                .name("请假审批")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
