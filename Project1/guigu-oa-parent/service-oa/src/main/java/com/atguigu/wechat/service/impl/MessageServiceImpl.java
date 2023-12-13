package com.atguigu.wechat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.OaProcess;
import com.atguigu.model.process.OaProcessTemplate;
import com.atguigu.model.system.SysUser;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.wechat.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.exception.WeChatOperationException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private SysUserService userService;
    @Autowired
    private OaProcessService processService;
    @Autowired
    private OaProcessTemplateService processTemplateService;
    @Autowired
    private WxMpService wxMpService;

    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        try {
            // 推送的目标用户
            SysUser armUser = userService.getById(userId);
            OaProcess process = processService.getById(processId);
            // 发送信息的用户
            SysUser submitUser = userService.getById(process.getUserId());
            OaProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
            // 设置推送对象
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(armUser.getOpenId())   // 要推送的用户的openId
                    .templateId("2DDDhxn6s-Yh0EBIpK590XyFX--5dlQilUJEweeTkAQ")      // 消息模板的id
                    // TODO
                    .url("http://8.222.179.20:9090/show/" + processId + "/" + taskId)     // 点击消息时跳转的地址
                    .build();
            templateMessage.addData(new WxMpTemplateData("first", submitUser.getName() + "提交了" + processTemplate.getName() + "申请"));
            templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode()));
            templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd:HH:mm:ss")));
            templateMessage.addData(new WxMpTemplateData("content", "单击此消息前往审批"));
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            throw new WeChatOperationException("推送信息失败：" + e.getMessage());
        }
    }
}
