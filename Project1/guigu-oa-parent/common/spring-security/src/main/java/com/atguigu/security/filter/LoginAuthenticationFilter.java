package com.atguigu.security.filter;

import com.alibaba.fastjson.JSON;
import com.atguigu.model.system.SysUser;
import com.atguigu.security.custom.MyUserDetails;
import com.atguigu.vo.system.LoginVo;
import com.common.exception.AuthenticationFailException;
import com.common.util.JwtHelp;
import com.common.result.Result;
import com.common.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 根据表单提交信息进行认证
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private StringRedisTemplate redisTemplate;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                     StringRedisTemplate redisTemplate) {
        // 该构造器关键就是获取并设置这个AuthenticationManager，用于调用该对象内部的方法进行认证
        this.setAuthenticationManager(authenticationManager);
        // 获取传入的RedisTemplate
        this.redisTemplate = redisTemplate;
        // 设置该过滤器拦截的登录的接口，以及指定拦截的是哪种请求方式
        this.setRequiresAuthenticationRequestMatcher
                (new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        // 是否只拦截POST请求
        this.setPostOnly(false);
    }

    /**
     * 获取表单信息后，委托认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            // 读取输入流，从而获取到表单提交的信息
            LoginVo vo = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginVo.class);
            // 将表单提交的信息封装为一个Authentication，用于与底层返回的UserDetails进行数据对比校验
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken
                            (vo.getUsername(), vo.getPassword());
            // 调用方法进行认证
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new AuthenticationFailException("读取表单提交信息失败：" + e.getMessage());
        }
    }

    /**
     * 认证成功时执行该方法，响应成功状态码，并响应token信息
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        // 获取到认证时用的UserDetails
        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();
        // 获取内部保存的SysUser（在数据库中查询获得到的）
        SysUser user = myUserDetails.getUser();
        // 生成token，并将token封装进统一结果集后，响应给客户端
        String token = JwtHelp.createToken(user.getId(), user.getUsername());
        Map<String, String> responseMsg = new HashMap<>();
        responseMsg.put("token", token);
        ResponseUtil.out(response, Result.ok(responseMsg));
        // 将用户的权限（List集合，转成JSON字符串后）缓存到Redis中
        redisTemplate.opsForValue().
                set(user.getUsername(),
                        JSON.toJSONString(myUserDetails.getAuthorities()));
    }

    /**
     * 认证失败时执行该方法，响应失败信息
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        // 获取失败的原因，并响应出去
        ResponseUtil.out(response, Result.fail().message(failed.getCause().getMessage()));
    }
}
