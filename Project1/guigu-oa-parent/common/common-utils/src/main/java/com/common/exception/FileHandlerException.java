package com.common.exception;

/**
 * 文件处理异常（上传）
 */
public class FileHandlerException extends RuntimeException {
    public FileHandlerException(String msg) {
        super(msg);
    }
}
