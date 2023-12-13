package com.common.util;

/**
 * 登录后将用户信息存储到ThreadLocal中，方便后续调用
 */
public class LoginUserInfoHelper {
    private static ThreadLocal<Long> userId = new ThreadLocal<>();
    private static ThreadLocal<String> username = new ThreadLocal<>();

    /**
     * 设置用户id到ThreadLocal中
     *
     * @param id 用户id
     */
    public static void setUserId(Long id) {
        userId.set(id);
    }

    /**
     * 获取ThreadLocal中保存的用户id
     *
     * @return userId
     */
    public static Long getUserId() {
        return userId.get();
    }

    /**
     * 设置ThreadLocal中的用户名
     *
     * @param name 用户名
     */
    public static void setUsername(String name) {
        username.set(name);
    }

    /**
     * 获取保存在ThreadLocal中的用户名
     *
     * @return username
     */
    public static String getUsername() {
        return username.get();
    }

    /**
     * 移除保存在ThreadLocal中的用户id和用户名
     */
    public static void removeInfo() {
        userId.remove();
        username.remove();
    }
}
