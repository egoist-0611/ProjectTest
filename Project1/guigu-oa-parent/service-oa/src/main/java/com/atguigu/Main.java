package com.atguigu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 启动类
@SpringBootApplication
// 正常情况下，我们是扫描common下的分页插件配置类的（虽然引入了依赖，但不处于同包或该类子包下），因此自定义包扫描路径
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
