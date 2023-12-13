package com.atguigu;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MaskUserListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("人事审批")) {
            delegateTask.setAssignee("july");
        } else {
            delegateTask.setAssignee("mary");
        }
    }
}
