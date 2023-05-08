package com.chengy.flowable.listener;


import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 自定义监听器
 */
public class MyListener implements TaskListener {

    /**
     * 监听器触发的方法
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("==========监听触发=========");
        System.out.println(delegateTask.getName());
        System.out.println(delegateTask.getEventName());
        System.out.println(delegateTask.getVariable("user1"));
        System.out.println("==========监听结束=========");
    }
}
