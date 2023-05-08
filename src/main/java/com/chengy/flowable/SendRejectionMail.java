package com.chengy.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("触发了SendRejectionMail");
        System.out.println("请假被拒绝,,,安心工作吧");
    }
}
