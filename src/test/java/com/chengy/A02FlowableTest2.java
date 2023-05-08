package com.chengy;


import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 部署一个自己创建的流程
 */
@SpringBootTest
public class A02FlowableTest2 {
    ProcessEngineConfiguration configurationMYSQL =null;
    @BeforeEach
    public void before1(){
        //流程引擎配置对象
        configurationMYSQL = new StandaloneProcessEngineConfiguration();
        //配置数据库链接信息
        configurationMYSQL.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configurationMYSQL.setJdbcUsername("root");
        configurationMYSQL.setJdbcPassword("pass123456");
        configurationMYSQL.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        configurationMYSQL.setDatabaseSchema(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//如果数据库没有表则自动创建

    }

    /**
     * 部署流程：部署请假流程
     */

    @Test
    public void testDepoly(){
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder  = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/holiday-request.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder .name("请求流程"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }


    @Test
    public void testDepolyStart(){
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        //RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        HashMap<String, Object> varibale = new HashMap<>();
        varibale.put("employee","张三");
        varibale.put("nrOfHolidays",3);
        varibale.put("desciption","请假理由啊啊啊啊");
        //根据id启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test1", varibale);
        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getActivityId() = " + processInstance.getActivityId());
        System.out.println("processInstance.getId() = " + processInstance.getId());


    }


    /**
     * 由于流程定义了用户，所以根据用户来查询
     */
    @Test
    public void testQueryTask(){
        // 获取流程引擎对象
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test1")
                .taskAssignee("user2")
                .singleResult();
        System.out.println(task);
    }


    /**
     * 查询到了任务，进行提交
     */
    @Test
    public void testCompleteTask(){
        // 获取流程引擎对象
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test1")
                .taskAssignee("user2")
                .singleResult();
        System.out.println(task);

        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("approved",false); // 拒绝请假
        // 完成任务
        taskService.complete(task.getId(),variables);
    }




}
