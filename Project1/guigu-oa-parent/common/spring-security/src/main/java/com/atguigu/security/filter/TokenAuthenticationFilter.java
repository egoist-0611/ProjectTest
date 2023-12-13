package com.atguigu.security.filter;

import com.alibaba.fastjson2.JSON;
import com.common.util.JwtHelp;
import com.common.result.Result;
import com.common.util.LoginUserInfoHelper;
import com.common.util.ResponseUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取请求头中的token进行认证
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private StringRedisTemplate redisTemplate;

    public TokenAuthenticationFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 拦截认证是否有token
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 判断请求头中是否存在token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            Long userId = JwtHelp.getUserId(token);
            String username = JwtHelp.getUsername(token);
            // 保存登录的用户信息到ThreadLocal中
            LoginUserInfoHelper.setUserId(userId);
            LoginUserInfoHelper.setUsername(username);
            // 根据用户名从Redis中获取保存的用户权限
            String jsonData = redisTemplate.opsForValue().get(username);
            // 将JSON字符串转换为List集合，每个元素以Map格式存储
            List<Map> authsMap = JSON.parseArray(jsonData, Map.class);
            // 重新定义一个存储权限的GrantedAuthority集合，将从Redis中读取到的权限存放进行
            List<GrantedAuthority> auths = new ArrayList<>();
            for (Map map : authsMap) {
                String auth = (String) map.get("authority");
                auths.add(new SimpleGrantedAuthority(auth));
            }
            // 生成Authentication认证信息，并在上下文中保存信息，之后，信息中的用户进行资源访问时，将不再进行认证，且在调用handler时，若有权限要求，则会进行权限的校验
            SecurityContextHolder.getContext()
                    .setAuthentication
                            (new UsernamePasswordAuthenticationToken
                                    (username, null, auths));
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        //  没有token，认证失败
        ResponseUtil.out(response, Result.fail().message("未登录认证"));
    }
}
