package com.chengy;


import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务分配
 * -值分配
 * 创建流程图的时候设置分配为${}，然后在variables中设置属性值，
 *
 * -表达式分配
 * 还可以是方法表达式，带参或者不带
 * ${printer.print()}
 * ${myBean.addNewOrder('orderName')}
 * ${myBean.doSomething(myVar, execution)}
 *
 * -监听器分配
 * 创建一个监听器，然后流程流转的时候会走监听器，在监听器中做出特殊处理
 */
@SpringBootTest
public class A04FlowableTest4 {

    /**
     * 测试方法
     */
    @Test
    public void testfenpei(){
        //根据
        //测试值分配
//        this.testFenPei("fenpei:1:27504");
        //分配表达式（集成spring的时候使用）
//        this.testFenPei("fenpeibiaoda:2:72504");
        //测试监听分配
//        this.testFenPei("fenpeijianting:1:57504");
    }


    /**
     * 部署新文件
     */
    @Test
    public void testDepoly(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder  = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/fenpeibiaoda.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder .name("分配表达式5"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        System.out.println(list);
        Deployment deployment1 = repositoryService.createDeploymentQuery().deploymentName("分配表达式5").list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult();
        System.out.println(processDefinition);

    }




    /**
     * 启动流程，并且指定了流程中分配的用户
     */
    @Test
    public void testFenPei(String id){
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 启动流程实例通过 RuntimeService 对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设置 assignee 的取值
        Map<String,Object> variables = new HashMap<>();
        variables.put("user1","张一") ;
        variables.put("user2","李二");
        variables.put("user3","赵三");
        // 启动流程实例，第一个参数是流程定义的id
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(id, variables);// 启动流程实例
        // 输出相关的流程实例信息
        System.out.println("流程定义的ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + processInstance.getId());
        System.out.println("当前活动的ID：" + processInstance.getActivityId());

    }


    /**
     * 获取流程引擎并查询任务、完成任务
     */
    @Test
    public void testgetLiuchengyinqing(){
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("赵三").list();
        for (Task task : taskList) {
            System.out.println(task);
            taskService.complete(task.getId());
        }
    }






}
