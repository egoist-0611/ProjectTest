package com.atguigu;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.spi.RegisterableService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ActivitiTest1 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Test
    public void test3(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/test03.bpmn20.xml")
                .name("监听器分配")
                .deploy();
        runtimeService.startProcessInstanceByKey("test03");
        List<Task> list = taskService.createTaskQuery().taskAssignee("july").list();
        for(Task task:list){
            System.out.println(task.getName());
        }
    }

    @Test
    public void test2() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/test02.bpmn20.xml")
                .name("表达式分配：组件方法")
                .deploy();
        runtimeService.startProcessInstanceByKey("test02");
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            System.out.println(task.getAssignee());
        }
    }

    @Test
    public void test1() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/test01.bpmn20.xml")
                .name("表达式分配：值")
                .deploy();
        Map<String, Object> map = new HashMap<>();
        map.put("mask1", "Tom");
        map.put("mask2", "Jack");
        runtimeService.startProcessInstanceByKey("test01", map);
        Task task = taskService.createTaskQuery()
                .taskAssignee("Tom")
                .singleResult();
        System.out.println(task.getName());
    }
}
