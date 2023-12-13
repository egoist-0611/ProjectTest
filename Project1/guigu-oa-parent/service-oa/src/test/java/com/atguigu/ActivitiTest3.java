package com.atguigu;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ActivitiTest3 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void test2() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("day", "2");
        runtimeService.startProcessInstanceByKey("test06");
    }

    @Test
    public void test3() {
        Task task = taskService.createTaskQuery().taskAssignee("Karry").singleResult();
//        taskService.complete(task.getId());
    }

    @Test
    public void test1() {
        repositoryService.createDeployment().addClasspathResource("process/test06.bpmn20.xml")
                .name("test06").deploy();
    }

    @Test
    public void test() {
        repositoryService.deleteDeployment("80c42489-8d1d-11ee-95d8-3613e85dfe49");
    }
}
