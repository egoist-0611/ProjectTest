package com.common.exception;

/**
 * 执行失败异常
 */
public class ExecFailException extends RuntimeException {
    public ExecFailException(String msg) {
        super(msg);
    }
}
