package com.atguigu;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class User {
    public String setMaskUser(int type) {
        if (type == 1) {
            return "zhangsan";
        }
        return "lisi";
    }
}
