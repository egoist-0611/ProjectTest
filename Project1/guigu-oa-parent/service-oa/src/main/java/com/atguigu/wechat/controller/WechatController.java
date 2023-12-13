package com.atguigu.wechat.controller;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.wechat.BindPhoneVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.exception.WeChatOperationException;
import com.common.result.Result;
import com.common.util.JwtHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Api(tags = "微信授权登录")
@Controller
@RequestMapping("/admin/wechat")
public class WechatController {
    // 授权成功后回调的地址（获取用户信息）
    private static final String userInfoUrl = "http://8.222.179.20:8800/admin/wechat/userInfo";

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private SysUserService userService;

    @ApiOperation("授权认证")
    @GetMapping("/authorize")
    public String authorize(@ApiParam("授权成功后返回原地址") @RequestParam String returnUrl, HttpServletRequest request) {
        // 授权成功后，返回原地址，并回调获取用户信息的地址（userInfoUrl）
        String redirectUrl = wxMpService.getOAuth2Service()
                .buildAuthorizationUrl(userInfoUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public String userInfo(@ApiParam("授权码") @RequestParam String code,
                           @ApiParam("重定向地址") @RequestParam("state") String returnUrl) {
        try {
            // 获取openId，当用户绑定了手机号时，会拥有该值
            String openId = wxMpService.getOAuth2Service().getAccessToken(code).getOpenId();
            // 获取指定openId对应的用户
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getOpenId, openId);
            SysUser user = userService.getOne(wrapper);
            // 当用户能获取到时，说明该用户已绑定手机号（若不能获取到，则需要跳转去绑定手机号）
            String token = "";
            if (user != null) {
                token = JwtHelp.createToken(user.getId(), user.getUsername());
                return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
            }
            // TODO
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        } catch (WxErrorException e) {
            throw new WeChatOperationException("获取用户信息失败：" + e.getMessage());
        }
    }

    @ApiOperation("绑定手机号")
    @PostMapping("/bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo vo) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, vo.getPhone());
        SysUser user = userService.getOne(wrapper);
        if (user != null) {
            user.setOpenId(vo.getOpenId());
            userService.updateById(user);
            return Result.ok(JwtHelp.createToken(user.getId(), user.getUsername()));
        } else {
            return Result.fail().message("不存在该手机号码的用户");
        }
    }
}



