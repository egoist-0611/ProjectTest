package com.common.result;

import lombok.Getter;

// 响应状态枚举，快速决定响应信息成功还是失败
@Getter         // 生成get方法，获取 状态码/状态信息
public enum ResultCodeEnum {
    // 相当于：public static final ResultCodeEnum SUCCESS = new ResultCodeEnum(200,"成功");
    // 将相同部分省略，并私有化构造器不让外界创建对象
    SUCCESS(200, "成功"),
    FAIL(201, "失败");

    private final Integer code;           // 状态码
    private final String message;         // 状态信息

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
