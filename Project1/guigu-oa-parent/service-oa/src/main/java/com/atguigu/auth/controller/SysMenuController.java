package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssignMenuVo;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService service;

    /**
     * 新增菜单
     *
     * @param menu SysMenu对象，封装了要添加的信息
     * @return 成功或失败的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result insert(@RequestBody SysMenu menu) {
        boolean res = service.save(menu);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 通过菜单id，修改菜单信息
     *
     * @param menu SysMenu对象，封装了要修改的菜单的id，及要修改的信息
     * @return 成功或失败的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation("修改菜单信息")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu menu) {
        boolean res = service.updateById(menu);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据菜单id，删除菜单
     *
     * @param id 要删除的菜单的id
     * @return 成功或失败的响应
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation("删除指定菜单")
    @DeleteMapping("/remove/{id}")
    public Result delete(@ApiParam("菜单ID") @PathVariable Long id) {
        service.removeMenuById(id);
        return Result.ok();
    }

    /**
     * 按层级方式，获取菜单
     *
     * @return SysMenu构成的集合，该集合中的每个SysMenu，会连接着其子层（若存在）
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("获取菜单")
    @GetMapping("/findNodes")
    public Result<List<SysMenu>> selectWithNodes() {
        List<SysMenu> list = service.selectWithNodes();
        return Result.ok(list);
    }

    /**
     * 获取所有的菜单信息（角色拥有的菜单信息会被选中）
     *
     * @param roleId 角色id
     * @return 所有的菜单信息（list）
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("获取指定角色所管理的菜单信息")
    @GetMapping("/toAssign/{roleId}")
    public Result<List<SysMenu>> getAssign(@PathVariable Long roleId) {
        List<SysMenu> menus = service.getMenuAndSelect(roleId);
        return Result.ok(menus);
    }

    /**
     * 为角色分配菜单
     *
     * @param vo 内含要修改的角色的id、要赋予该角色的菜单的id
     * @return 成功响应
     */
    @PreAuthorize("hasAuthority('bnt.sysMenu.assignAuth')")
    @ApiOperation("为角色分配菜单")
    @PostMapping("/doAssign")
    public Result setAssign(@RequestBody AssignMenuVo vo) {
        service.setAssign(vo);
        return Result.ok();
    }
}
