package com.atguigu.auth.util;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.MetaVo;
import com.atguigu.vo.system.RouterVo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// 构建菜单层级关系
public class MenuHelper {
    /**
     * 构建菜单的层级关系
     *
     * @param sysMenus // 原先所有的菜单（List）
     * @return // 构建好的菜单
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenus) {
        List<SysMenu> menuWithNodes = new ArrayList<>();
        /* 思路：递归
            菜单中，parent_id为0的，则为最大的菜单项
            从最大的菜单项入手，判断所有的菜单中，是否有parent_id=该最大菜单项id的
                若有，则递归，接着判断是否有parent_id=该菜单项的
                若没有，则跳出递归，将该菜单项（SysMenu）添加为其父菜单项的children中
         * */
        for (SysMenu menu : sysMenus) {
            // 递归入口：parent_id为0，则调用next()进行递归
            if (menu != null && menu.getParentId() == 0) {
                menuWithNodes.add(next(menu, sysMenus));
            }
        }
        return menuWithNodes;
    }

    /**
     * 递归方法
     *
     * @param parentMenu 将遍历所有的菜单，将其parent_id和该parentMenu中的id进行比较，相同则说明该parent_id是子菜单，接下来应递归该parent_id
     * @param sysMenus   所有的菜单项
     * @return 已经设置好children（分好了层级）的SysMenu对象
     */
    public static SysMenu next(SysMenu parentMenu, List<SysMenu> sysMenus) {
        // 进来之后，先开辟存储子层菜单的空间（且确保该层菜单的子层菜单所用的空间是相同地址）
        parentMenu.setChildren(new ArrayList<>());
        for (SysMenu menu : sysMenus) {
            if (parentMenu.getId().longValue() == menu.getParentId().longValue()) {
                parentMenu.getChildren().add(next(menu, sysMenus));
            }
        }
        return parentMenu;
    }

    /**
     * 根据菜单信息，构建前端路由所需要的信息（递归构建）
     *
     * @param menus 有层级的菜单信息
     * @return RouterVo集合，由多个目录级菜单构成
     */
    public static List<RouterVo> buildRouter(List<SysMenu> menus) {
        // 用来存储 每一层的 每一个router菜单 的数据（除0层外，之后创建的routerInfo，都是上层router的子路由）
        List<RouterVo> routerInfo = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            // 设置path：目录级别菜单需要以 / 开头
            String path = menu.getPath();
            if (menu.getParentId() == 0) {
                path = "/" + path;
            }
            router.setPath(path);
            router.setHidden(false);
            router.setAlwaysShow(false);       // 默认应为false，因为最后一层不应该具有可折叠
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            // 若类型为1，则说明为菜单类型，菜单类型下的children都为按钮（component值为null），不需要设置路由信息
            // 但是，若存在component值不为null的，则说明为隐藏路由（hidden=true），需要配置路由信息
            if (menu.getType() == 1) {
                for (SysMenu child : children) {
                    if (!StringUtils.isEmpty(child.getComponent())) {
                        RouterVo hiddenRouter = new RouterVo();
                        hiddenRouter.setHidden(true);
                        hiddenRouter.setPath(path);
                        hiddenRouter.setAlwaysShow(false);
                        hiddenRouter.setComponent(child.getComponent());
                        hiddenRouter.setMeta(new MetaVo(child.getName(), child.getIcon()));
                        routerInfo.add(hiddenRouter);
                    }
                }
                // 非菜单类型，递归创建路由信息，直到最后在菜单类型时退出路由的构建（因为菜单类型时会走上面，走完之后直接退出递归了（按钮也确实是最后一层））
            } else {
                // 该条件判断不可少：若是只有目录类型的情况
                if (!CollectionUtils.isEmpty(children)) {
                    router.setAlwaysShow(true);     // 只要不是最后一层的，就可以为alwaysShow=true
                    router.setChildren(buildRouter(children));          // 递归调用，每轮递归结束后的路由信息，将作为子路由
                }
            }
            // 将路由信息保存（比如：最后一层时，除了隐藏路由，还应该有菜单类型的路由）
            routerInfo.add(router);
        }
        return routerInfo;
    }
}
