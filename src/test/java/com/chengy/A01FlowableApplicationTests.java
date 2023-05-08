package com.chengy;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 入门
 */
@SpringBootTest
public class A01FlowableApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 获取流程引擎对象
     */
    @Test
    void testProcessEngine() {
        //流程引擎配置对象
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        //配置数据库链接信息
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("pass123456");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        configuration.setDatabaseSchema(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//如果数据库没有表则自动创建
        //创建流程引擎对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }


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
        //创建流程引擎对象
    }

    ProcessEngineConfiguration configurationDM =null;
//    @BeforeEach
    public void before2(){
        //流程引擎配置对象
        configurationDM = new StandaloneProcessEngineConfiguration();
        //配置数据库链接信息
        configurationDM.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configurationDM.setJdbcUsername("elec");
        configurationDM.setJdbcPassword("123456789");
        configurationDM.setJdbcUrl("jdbc:dm://118.31.168.90:5236");
        configurationDM.setDatabaseSchema(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//如果数据库没有表则自动创建
        //创建流程引擎对象
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

    /**
     * 查询流程信息
     * 根据部署的deployment的id查询出流程的其他信息
     */
    @Test
    public void testDepolyQuery(){
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery.deploymentId("7501").singleResult();
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getId() = " + processDefinition.getId());
        System.out.println("processDefinition.getKey() = " + processDefinition.getKey());
    }

    /**
     * 删除流程
     */
    @Test
    public void testDepolyDelete(){
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.deleteDeployment("2501");//用id来删除，如果部署的流程启动了就不允许删除了
        repositoryService.deleteDeployment("2501",true);//及联删除，如果流程启动了也可以删除
    }


    /**
     * 启动流程
     */
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", varibale);
        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getActivityId() = " + processInstance.getActivityId());
        System.out.println("processInstance.getId() = " + processInstance.getId());


    }


    /**
     * 处理流程，根据zhangsan查询到对应的任务，然后设置变量，并且用complete进行提交
     */
    @Test
    public void testCompleteTask(){
        // 获取流程引擎对象
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println(task);
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("approved",false); // 拒绝请假
        // 完成任务
        taskService.complete(task.getId(),variables);

    }

    /**
     * 查询历史任务
     * 历史任务中的数据
     * 例如一个流程花了多少时间
     */
    @Test
    public void queryHistory(){
        ProcessEngine processEngine = configurationMYSQL.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("holidayRequest:1:7503")
                .finished()//查询已经完成的任务
                .orderByHistoricActivityInstanceEndTime().asc()//根据流程实例结束时间进行排序
                .list();
        for (HistoricActivityInstance history : list) {
            System.out.println(history.getActivityId() + " took "
                    + history.getDurationInMillis() + " milliseconds");
        }

    }

}
