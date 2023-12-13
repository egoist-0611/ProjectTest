package com.common.exception;

/**
 * 微信操作异常
 */
public class WeChatOperationException extends RuntimeException {
    public WeChatOperationException(String msg) {
        super(msg);
    }
}
