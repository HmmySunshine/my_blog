package com.fanzehao.blogsystem.response;

public class Result<T>{
    private Integer code;
    private String message;
    private T data;
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message, null);
    }
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    public static Result fail() {
        return new Result<>(500, "服务内部错误", null);
    }
    public static Result fail(String message) {
        return new Result<>(500,message, null);
    }
    public static Result fail(Integer code,String message) {
        return new Result<>(code,message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
