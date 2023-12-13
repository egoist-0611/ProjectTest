package com.atguigu.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.model.wechat.Menu;
import com.atguigu.wechat.mapper.MenuMapper;
import com.atguigu.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.exception.MenuOperationException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private WxMpService wxMpService;

    @Override
    public List<Menu> findMenuInfo() {
        List<Menu> menus = new ArrayList<>();
        // 1.获取所有的菜单
        List<Menu> menuList = baseMapper.selectList(null);
        // 2.获取所有的一级菜单
        List<Menu> firstMenus = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() == 0) {
                firstMenus.add(menu);
            }
        }
        // 3.遍历所有菜单，找到某个一级菜单的所有子层菜单
        for (Menu firstMenu : firstMenus) {
            List<Menu> children = new ArrayList<>();
            for (Menu menu : menuList) {
                if (firstMenu.getId().longValue() == menu.getParentId().longValue()) {
                    children.add(menu);
                }
            }
            firstMenu.setChildren(children);
            // 4.保存一级菜单（该一级菜单下已连接了其子层菜单）
            menus.add(firstMenu);
        }
        return menus;
    }

    @Override
    public void syncMenu() {
        List<Menu> menus = this.findMenuInfo();
        // 最终响应的数据
        JSONObject jsonData = new JSONObject();
        // 总菜单数据（一级菜单下连着二级菜单）
        JSONArray menuJson = new JSONArray();
        for (Menu menu : menus) {
            JSONObject firstMenuJson = new JSONObject();
            firstMenuJson.put("name", menu.getName());
            // 有无子层菜单
            if (CollectionUtils.isEmpty(menu.getChildren())) {
                firstMenuJson.put("type", menu.getType());
                if ("view".equals(menu.getType())) {
                    firstMenuJson.put("url", "http://oa.atguigu.cn/#" + menu.getUrl());
                } else {
                    firstMenuJson.put("key", menu.getMenuKey());
                }
            } else {
                // 二级菜单数据
                JSONArray subButton = new JSONArray();
                for (Menu child : menu.getChildren()) {
                    JSONObject secondMenuJson = new JSONObject();
                    secondMenuJson.put("name", child.getName());
                    secondMenuJson.put("type", child.getType());
                    if ("view".equals(child.getType())) {
                        // TODO:可能有错，路径与文档不同
                        secondMenuJson.put("url", "http://oa.atguigu.cn/#" + child.getUrl());
                    } else {
                        secondMenuJson.put("key", child.getMenuKey());
                    }
                    subButton.add(secondMenuJson);
                }
                firstMenuJson.put("sub_button", subButton);
            }
            menuJson.add(firstMenuJson);
        }
        jsonData.put("button", menuJson);
        try {
            // 推送菜单（响应JSON数据）
            wxMpService.getMenuService().menuCreate(jsonData.toJSONString());
        } catch (WxErrorException e) {
            throw new MenuOperationException("菜单推送异常：" + e.getMessage());
        }
    }
}
