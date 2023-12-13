package com.atguigu.wechat.service;

import com.atguigu.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    /**
     * 获取公众号菜单
     *
     * @return 层级菜单
     */
    List<Menu> findMenuInfo();

    /**
     * 推送菜单到微信公众号
     */
    void syncMenu();
}
