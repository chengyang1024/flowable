package com.chengy;


import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 局部流程变量
 */
@SpringBootTest
public class A05Flowable流程变量Test {
    @Test
    public void testDepoly(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder  = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/流程变量.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder .name("流程变量1"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        System.out.println(list);
        Deployment deployment1 = repositoryService.createDeploymentQuery().deploymentName("流程变量1").list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult();
        System.out.println(processDefinition);

    }


    /**
     * 启动流程
     * @param
     */
    @Test
    public void testbianliang(){
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 启动流程实例通过 RuntimeService 对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设置 assignee 的取值
        Map<String,Object> variables = new HashMap<>();
        variables.put("num",3) ;
        // 启动流程实例，第一个参数是流程定义的id
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("liuchengbianliang:1:82504", variables);// 启动流程实例
    }


    /**
     * 获取流程引擎并查询任务、完成任务
     */
    @Test
    public void testgetLiuchengyinqing(){
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskId("85006").list();
        for (Task task : taskList) {
            System.out.println(task);
//            HashMap<String, Object> value = new HashMap<>();
//            value.put("num",2);
            //设置局部流程变量
//            taskService.setVariablesLocal(task.getId(),value);
            taskService.complete(task.getId());
        }
    }

}
