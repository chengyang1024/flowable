package com.chengy;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class A06候选人候选组Test {
    @Test
    public void testDepoly() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("process/候选组.bpmn20.xml");// 添加流程部署文件
        deploymentBuilder.name("候选组1"); // 设置部署流程的名称
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        System.out.println(list);
        Deployment deployment1 = repositoryService.createDeploymentQuery().deploymentName("候选组1").list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult();
        System.out.println(processDefinition);

    }


    /**
     * 启动流程
     *
     * @param
     */
    @Test
    public void testbianliang() {
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 启动流程实例通过 RuntimeService 对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设置 assignee 的取值
        Map<String, Object> variables = new HashMap<>();
        variables.put("user1", "zhangsan");
        variables.put("user2", "lisi");
        variables.put("user3", "wangwu");
        variables.put("user4", "zhaoliu");
        // 启动流程实例，第一个参数是流程定义的id
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("houxuanren:1:92504", variables);// 启动流程实例
    }


    /**
     * 作为候选人之一，可以查询到当前节点的任务，并且完成
     */
    @Test
    public void testgetrenwu() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //根据候选人查询到自己的任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("houxuanren:1:92504").taskCandidateUser("zhaoliu").list();
        System.out.println("taskList.size() = " + taskList.size());
        for (Task task : taskList) {
            System.out.println(task);
            taskService.complete(task.getId());
        }
    }


    /**
     * 拾取任务
     */
    @Test
    public void testgetrenwu2() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //根据候选人查询到自己的任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("houxuanren:1:92504").taskCandidateUser("wangwu").list();
        System.out.println("taskList.size() = " + taskList.size());
        for (Task task : taskList) {
            System.out.println(task);
            //拾取任务，拾取之后assignee就指定了人，从候选人就查询不到了
            taskService.claim(task.getId(), "wangwu");
        }
    }


    /**
     * 退还任务
     */
    @Test
    public void testgetrenwu3() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //根据候选人查询到自己的任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("houxuanren:1:92504").taskAssignee("wangwu").list();
        System.out.println("taskList.size() = " + taskList.size());
        for (Task task : taskList) {
            System.out.println(task);
            //归还任务
            taskService.unclaim(task.getId());
        }
    }


    /**
     * 交接任务
     */
    @Test
    public void testgetrenwu4() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //根据候选人查询到自己的任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("houxuanren:1:92504").taskAssignee("wangwu").list();
        System.out.println("taskList.size() = " + taskList.size());
        for (Task task : taskList) {
            System.out.println(task);
            //交接任务
            taskService.setAssignee(task.getId(), "lisi");
        }
    }
//-------------------------------------------------候选人组------------------------------------------------
    //候选人组需要在flowable中设置用户和组，并且将信息加到flowable表中，实际应用场景需要将系统的用户同步到flowable用户中


    /**
     * 维护一下组和用户信息
     */
    @Test
    public void testMaintenance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //创建组
        for (int i = 0; i < 2; i++) {
            Group group = identityService.newGroup("group" + i);
            group.setName(i % 2 != 0 ? "奇数组" : "偶数组");
            identityService.saveGroup(group);
        }
        //创建用户并加入到组
        for (int i = 0; i < 10; i++) {

            User user = identityService.newUser("user" + i);
            user.setFirstName("user");
            user.setLastName(String.valueOf(i));
            identityService.saveUser(user);
            if (i % 2 == 0) {
                identityService.createMembership(user.getId(), "group0");
            } else {
                identityService.createMembership(user.getId(), "group1");

            }

        }

        Group caiwu = identityService.newGroup("caiwu");
        caiwu.setName("财务");
        identityService.saveGroup(caiwu);
        User caiwu1 = identityService.newUser("caiwu1");
        caiwu1.setFirstName("财务");
        caiwu1.setLastName("1");
        identityService.saveUser(caiwu1);
        identityService.createMembership("caiwu1", "caiwu");

    }


    /**
     * 执行流程
     */
    @Test
    public void testGroup() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> variables = new HashMap<>();
        variables.put("xiaoshoubu","group0") ;
        variables.put("zhuguan","group1") ;
        variables.put("caiwu","caiwu") ;
        runtimeService.startProcessInstanceById("houxuanzu1:1:117504",variables);
    }

    /**
     *
     * 设置好了组，然后只要是组内的成员都可以查询到任务
     * 查询到所属的任务
     */
    @Test
    public void testQueryTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> group1 = taskService.createTaskQuery().taskCandidateUser("user6").list();
        System.out.println(group1);

    }




    }
