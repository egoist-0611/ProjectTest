package com.atguigu;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ActivitiTest2 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void test2() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/test04.bpmn20.xml")
                .name("test04")
                .deploy();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test04");
        List<Task> candidateTasks = taskService.createTaskQuery().taskCandidateUser("04").list();
        for (Task task : candidateTasks) {
            if (task != null) {
                taskService.claim(task.getId(), "04");
            }
        }
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("04").list();
        for (Task t : tasks) {
            taskService.complete(t.getId());
        }
    }

    @Test
    public void test1() {
        repositoryService.createDeployment()
                .addClasspathResource("process/test01.bpmn20.xml")
                .name("test01")
                .deploy();
        Map<String, Object> map = new HashMap<>();
        map.put("mask1", "01");
        runtimeService.startProcessInstanceByKey("test01", map);
        Task task = taskService.createTaskQuery().taskAssignee("01").singleResult();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("mask2", "02");
        taskService.complete(task.getId(), map1);
        Task task1 = taskService.createTaskQuery().taskAssignee("02").singleResult();
        System.out.println(task1.getAssignee());
    }
}
