package com.chengy;


import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 流程引擎的获取方式
 */
@SpringBootTest
public class A03PeocessEngineTest {


    @Test
    public void before1() {
        ProcessEngineConfiguration configurationMYSQL = null;
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
     * 获取流程引擎，加载默认的文件
     */
    @Test
    public void testPeocessEngine1(){
        //创建默认的PeocessEngine流程引擎
        //默认访问flowable.cfg.xml文件，与上面的before1方法效果一样
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

    }

    /**
     * 获取流程引擎，使用自定义的文件名
     */
    @Test
    public void testPeocessEngine2(){
        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml");
        ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();
        System.out.println(processEngine);


    }
}
