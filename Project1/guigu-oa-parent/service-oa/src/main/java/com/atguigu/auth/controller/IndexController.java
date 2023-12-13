package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysUserService;
import com.common.exception.LoginFailException;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.common.util.JwtHelp;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/index")
@Api(tags = "登录功能")
public class IndexController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     * @param vo    登录信息（账号密码）
     * @return  Map，内含一个token
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<Map<String,String>> login(@RequestBody LoginVo vo) {
        // 校验登录用户是否合法
        SysUser user = userService.getUserByUsername(vo.getUsername());
        if (user == null) {
            throw new LoginFailException("用户不存在");
        }
        if (!user.getPassword().equals(passwordEncoder.encode(vo.getPassword()))) {
            throw new LoginFailException("密码错误");
        }
        if (user.getStatus() == 0) {
            throw new LoginFailException("账号停用");
        }
        // 生成token并返回
        Map<String, String> map = new HashMap<>();
        map.put("token", JwtHelp.createToken(user.getId(), user.getUsername()));
        return Result.ok(map);
    }

    /**
     * 获取当前登录用户的信息（包括其可操作的菜单及其按钮（0、1：菜单 2：按钮））
     * @param token 含有用户id信息的token
     * @return  Map，内含用户名、用户角色、用户菜单按钮操作权限、路由信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<Map<String,Object>> info(@RequestHeader("token") String token) {
        Map<String, Object> map = userService.getUserInfo(JwtHelp.getUserId(token));
        return Result.ok(map);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }
}
