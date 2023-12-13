package com.atguigu.common.handler;

import com.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 全局异常处理类
//@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     * @param e 异常信息保存对象
     * @return  统一格式Result，自定义message状态信息
     */
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        return Result.fail().message("Exception异常");
    }
}
