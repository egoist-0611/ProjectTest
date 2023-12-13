package com.atguigu.wechat.service;

public interface MessageService {
    /**
     * 推送待处理信息
     *
     * @param processId 模板信息id
     * @param userId    目标用户id
     * @param taskId    任务id
     */
    public void pushPendingMessage(Long processId, Long userId, String taskId);
}
