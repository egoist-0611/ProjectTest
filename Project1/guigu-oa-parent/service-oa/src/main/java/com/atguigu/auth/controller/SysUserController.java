package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.AssignRoleVo;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据条件（用户名或用户创建时间点）查询并分页
     *
     * @param page  当前显示的页码
     * @param limit 每页显示的条目数
     * @param vo    封装条件的对象（用户名、用户创建的时间点）
     * @return Page类型数据
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")       // 是否有权限调用handler
    @ApiOperation("根据条件查询并分页")
    @GetMapping("/{page}/{limit}")
    public Result<Page<SysUser>> pageQuerySelect(@ApiParam("显示哪页") @PathVariable Long page,
                                                 @ApiParam("每页显示几条") @PathVariable Long limit,
                                                 @ApiParam("条件：用户名/创建时间点匹配") SysUserQueryVo vo) {
        Page<SysUser> pageObj = new Page<>(page, limit);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String keyword = vo.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {         // 当传递了匹配username的keyword条件时，添加该条件
            wrapper.like(SysUser::getUsername, keyword);
        }
        String createTimeBegin = vo.getCreateTimeBegin();
        String createTimeEnd = vo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(createTimeBegin) && !StringUtils.isEmpty(createTimeEnd)) {        // 当传递了匹配createTime的条件时，添加该条件
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
            wrapper.le(SysUser::getCreateTime, createTimeEnd);
        }
        Page<SysUser> pageRes = service.page(pageObj, wrapper);
        return Result.ok(pageRes);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return SysUser用户信息对象
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("根据用户ID查询")
    @GetMapping("/get/{id}")
    public Result<SysUser> selectById(@ApiParam("用户ID") @PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    /**
     * 添加新用户
     *
     * @param user SysUser用户对象，内含用户的信息
     * @return 失败或成功的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation("添加新用户")
    @PostMapping("/save")
    public Result insert(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean res = service.save(user);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据用户ID修改用户信息
     *
     * @param user SysUser用户对象，内含要修改的用户id，及要修改为的信息
     * @return 成功或失败的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("根据ID修改用户信息")
    @PutMapping("/modify")
    public Result update(@RequestBody SysUser user) {
        boolean res = service.updateById(user);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id 用户id
     * @return 成功或失败的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation("根据ID删除用户信息")
    @DeleteMapping("/remove/{id}")
    public Result delete(@ApiParam("用户ID") @PathVariable Long id) {
        boolean res = service.removeById(id);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 修改用户状态
     *
     * @param id     要修改的用户的id
     * @param status 状态：1可用 0停用
     * @return 成功的响应码
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("修改用户状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@ApiParam("用户ID") @PathVariable Long id,
                               @ApiParam("状态（1：正常 0：停用）") @PathVariable Integer status) {
        service.updateStatus(id, status);
        return Result.ok();
    }
}
