package com.common.exception;

/**
 * 登录失败异常
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String msg) {
        super(msg);
    }
}
