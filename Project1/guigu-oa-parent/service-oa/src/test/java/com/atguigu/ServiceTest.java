package com.atguigu;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private SysRoleService service;
    @Test
    public void selectAllTest(){
        List<SysRole> roles = service.list();
        System.out.println(roles);
    }
}
