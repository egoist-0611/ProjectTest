package com.atguigu.wechat.controller;

import com.atguigu.model.wechat.Menu;
import com.atguigu.wechat.service.MenuService;
import com.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "微信公众号菜单接口")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    private MenuService service;

    @ApiOperation("获取所有菜单")
    @GetMapping("/findMenuInfo")
    public Result<List<Menu>> findMenuInfo() {
        List<Menu> list = service.findMenuInfo();
        return Result.ok(list);
    }

    @ApiOperation("获取指定菜单")
    @GetMapping("/get/{id}")
    public Result<Menu> get(@ApiParam("菜单id") @PathVariable Long id) {
        Menu menu = service.getById(id);
        return Result.ok(menu);
    }

    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        boolean res = service.save(menu);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result updateById(@RequestBody Menu menu) {
        boolean res = service.updateById(menu);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@ApiParam("菜单id") @PathVariable Long id) {
        boolean res = service.removeById(id);
        if (res) {
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation("推送菜单")
    @GetMapping("syncMenu")
    public Result createMenu() {
        service.syncMenu();
        return Result.ok();
    }
}
