package com.common.result;

import lombok.Data;

// 统一响应的数据类型
@Data       // 生成get、set方法（由于静态方法没有this对象可调用，因此只能通过get、set方法赋值）
public class Result<T> {
    private Integer code;       // 状态码
    private String message;     // 状态信息
    private T data;         // 数据

    // 调用后，创建并返回封装好的Result对象
    private static <T> Result<T> build(T data, ResultCodeEnum codeEnum) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(codeEnum.getCode());
        result.setMessage(codeEnum.getMessage());
        return result;
    }

    /**
     * 快速构建成功状态的响应
     *
     * @return 响应内容包含：状态码-200、状态信息
     */
    public static Result ok() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 快速构建成功状态的响应
     *
     * @param data 响应数据
     * @param <T>  响应数据的类型
     * @return 响应内容包含：状态码-200、状态信息、响应数据
     */
    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 快速构建失败状态的响应
     *
     * @return 响应内容包含：状态码-201、状态信息
     */
    public static Result fail() {
        return build(null, ResultCodeEnum.FAIL);
    }

    /**
     * 自定义响应的状态信息
     *
     * @param message 自定义的状态信息
     * @return 调用该方法的Result
     */
    public Result<T> message(String message) {
        this.message = message;
        return this;
    }
}
