package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssignRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/sysRole")
@Api(tags = "角色管理")
public class SysRoleController {
    @Autowired
    private SysRoleService service;
    @Autowired
    private SysUserRoleService userRoleService;

    /**
     * 查询所有的角色
     *
     * @return 统一格式Result，内部封装了List<SysRole>集合数据
     */
    @GetMapping("/findAll")
    @ApiOperation("获取所有角色")
    public Result<List<SysRole>> selectAll() {
        List<SysRole> roles = service.list();
        return Result.ok(roles);
    }

    /**
     * 根据条件（角色名称）进行查询并分页
     *
     * @param page  显示第几页
     * @param limit 每页显示的条目数
     * @param vo    封装条件的对象（角色名称）
     * @return 统一格式Result，内部封装了Page对象数据
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/{page}/{limit}")
    @ApiOperation("根据条件查询并分页")
    public Result<Page<SysRole>> pageQuerySelect(@ApiParam("显示哪页") @PathVariable Long page,
                                                 @ApiParam("每页显示几条") @PathVariable Long limit,
                                                 @ApiParam("条件：名称匹配") SysRoleQueryVo vo) {
        Page<SysRole> pageObj = new Page<>(page, limit);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = vo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {     // 判断roleName是否为null或为""
            wrapper.like(SysRole::getRoleName, roleName);
        }
        Page<SysRole> pageRes = service.page(pageObj, wrapper);
        return Result.ok(pageRes);
    }

    /**
     * 根据id查询角色
     *
     * @param id 角色id
     * @return 统一格式Result, 内部封装了SysRole对象数据
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/get/{id}")
    @ApiOperation("根据角色ID查询")
    public Result<SysRole> selectById(@ApiParam("角色ID") @PathVariable Long id) {
        SysRole role = service.getById(id);
        return Result.ok(role);
    }


    /**
     * 添加新角色
     *
     * @param role SysRole对象，内含要添加的新角色的信息
     * @return 统一格式Result，失败或成功响应
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PostMapping("/save")
    @ApiOperation("添加新角色")
    public Result insert(@RequestBody SysRole role) {
        boolean res = service.save(role);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 修改角色信息
     *
     * @param role SysRole对象，内含要修改的角色的id，以及要修改的信息
     * @return 统一格式Result，失败或成功响应
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PutMapping("/update")
    @ApiOperation("根据ID修改角色信息")
    public Result update(@RequestBody SysRole role) {
        boolean res = service.updateById(role);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据角色ID删除指定角色
     *
     * @param id 角色id
     * @return 统一格式Result，失败或成功响应
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/remove/{id}")
    @ApiOperation("根据ID删除角色信息")
    public Result delete(@ApiParam("角色ID") @PathVariable Long id) {
        boolean res = service.removeById(id);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据ID批量删除角色信息
     *
     * @param ids 含多个id的数组
     * @return 统一格式Result，失败或成功响应
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/batchRemove")
    @ApiOperation("根据ID批量删除角色")
    public Result batchDelete(@RequestBody List<Long> ids) {
        boolean res = service.removeByIds(ids);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 获取所有的角色及指定用户的角色
     *
     * @param id 用户id
     * @return Map集合，内含所有的角色、指定用户的所有角色
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("获取所有的角色及指定用户的角色")
    @GetMapping("/toAssign/{id}")
    public Result<Map<String, List<SysRole>>> getAssign(@ApiParam("用户ID") @PathVariable Long id) {
        Map<String, List<SysRole>> map = userRoleService.getAssign(id);
        return Result.ok(map);
    }

    /**
     * 修改用户拥有的角色
     *
     * @param vo 用户id，及要修改为的角色的id
     * @return 成功的响应码
     */
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("修改用户所拥有的角色")
    @PostMapping("/doAssign")
    public Result setAssign(@RequestBody AssignRoleVo vo) {
        userRoleService.setAssign(vo);
        return Result.ok();
    }
}
