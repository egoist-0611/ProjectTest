package com.atguigu;

import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class OtherTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void test1(){
        redisTemplate.opsForValue().set("role",new SysRole("jojf","efw","rere"));
        Object o = redisTemplate.opsForValue().get("role");
        System.out.println(o.getClass());
        System.out.println(o);
    }
}
