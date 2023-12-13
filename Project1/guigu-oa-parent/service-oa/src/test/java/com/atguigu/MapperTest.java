package com.atguigu;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MapperTest {
    @Autowired
    private SysRoleMapper mapper;

    @Test
    public void selectAllTest() {
        List<SysRole> roles = mapper.selectList(null);
        System.out.println(roles);
    }

    @Test
    public void insertTest(){
        SysRole role = new SysRole("Mike", "manager", "管理员");
        int res = mapper.insert(role);
        System.out.println(role.getId());
        System.out.println(role.getCreateTime());
        System.out.println(res);
    }

    @Test
    public void updateTest(){
        SysRole role = mapper.selectById(5);
        role.setRoleName("Rose");
        mapper.updateById(role);
    }

    @Test
    public void deleteTest(){
        mapper.deleteById(5);
    }

    @Test
    public void deleteMoreTest(){
        int res = mapper.deleteBatchIds(Arrays.asList(1, 2));
        System.out.println(res);
    }

    @Test
    public void SelectByQuery(){
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(SysRole::getId,"3");
        List<SysRole> roles = mapper.selectList(wrapper);
        System.out.println(roles);
    }
}
