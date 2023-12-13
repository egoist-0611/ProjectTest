package com.common.exception;

/**
 * 认证失败异常
 */
public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException(String msg) {
        super(msg);
    }
}
