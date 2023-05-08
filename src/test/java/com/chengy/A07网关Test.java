package com.chengy;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class A07网关Test {

    //--------排他网关
    @Test
    public void testDepoly() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/网关-排他网关.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder.name("排他网关1"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        System.out.println(list);
        Deployment deployment1 = repositoryService.createDeploymentQuery().deploymentName("排他网关1").list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult();
        System.out.println(processDefinition);

    }
    /**
     * 启动排他网关
     */
    @Test
    public void testPaita(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.startProcessInstanceById("paitawangguan1:1:130004");
        runtimeService.startProcessInstanceById("bingxingwangguan1:1:142504");
        runtimeService.startProcessInstanceById("bingxingwangguan1:1:142504");

    }

    /**
     * 小王提交流程,并提交请假天数
     */
    @Test
    public void testPaita1(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("num",4);
        Task task = taskService.createTaskQuery().taskAssignee("xiaowang").singleResult();
        taskService.complete(task.getId(),variables);
    }


    /**
     * 流程到了总经理
     */
    @Test
    public void testPaita2(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("zongjingli").singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 流程到了部门经理
     */
    @Test
    public void testPaita3(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("bumenjingli").singleResult();
        taskService.complete(task.getId());
    }


    /**
     * 流程到了人事
     */
    @Test
    public void testPaita4(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("renshi").singleResult();
        taskService.complete(task.getId());
    }


    //--------并行网关-------
    @Test
    public void testDepoly2() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/并行网关3.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder.name("并行网关3"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        System.out.println(list);
        Deployment deployment1 = repositoryService.createDeploymentQuery().deploymentName("并行网关3").list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult();
        System.out.println(processDefinition);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceById(processDefinition.getId());

    }

    /**
     * 流程到了技术经理
     */
    @Test
    public void testPaita5(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("jishujingli").singleResult();
        taskService.complete(task.getId());
    }

}
