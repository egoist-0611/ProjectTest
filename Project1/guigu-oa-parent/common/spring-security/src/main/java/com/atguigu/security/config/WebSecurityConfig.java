package com.atguigu.security.config;

import com.atguigu.security.custom.MyUserDetailsService;
import com.atguigu.security.filter.LoginAuthenticationFilter;
import com.atguigu.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 认证配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    // 注入StringRedisTemplate，由于自定义RedisTemplate的话，从Redis获取JSON数据时，SimpleGrantedAuthority没有提供一个无参构造，因此无法还原为Java对象
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取一个AuthenticationManager
     */
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 添加一个密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置拦截认证规则
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    // 关闭csrf跨站请求伪造
                .csrf().disable()
                // 允许跨域
                .cors()
                .and()
                // 所有请求都需要经过认证
                .authorizeRequests().anyRequest().authenticated()
                .and()
                // 设置登录认证时使用的filter（该filter需要用到AuthenticationManager（用于委托认证）、RedisTemplate（用于缓存用户权限），因为该filter没有存入IOC容器中，因此需要传入）
                .addFilter(new LoginAuthenticationFilter(authenticationManager(), redisTemplate))
                // 设置其他认证（token认证）时使用的filter，并指定该filter是在UsernamePasswordAuthenticationFilter（登录过滤）之后才进行拦截的
                .addFilterAfter(new TokenAuthenticationFilter(redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);

        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * 配置自定义的loadUserByUsername认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 定义哪些请求不需要进行拦截
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/admin/modeler/**", "/diagram-viewer/**", "/editor-app/**", "/*.html", "/admin/processImage/**",
                "/admin/wechat/authorize", "/admin/wechat/userInfo", "/admin/wechat/bindPhone",
                "/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html", "/v3/**");
    }
}
