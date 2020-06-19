package com.zq.xijue.core;

/**
 * @author zhangQ
 * @description 自定义统一响应体
 */
public class ResultVO<T> {
    private int code;
    private String msg;
    private boolean success;
    private T data;

    public ResultVO(boolean success, String info) {
        this.success = success;
        msg = info;
    }

    public ResultVO(T data) {
        this(true, ResultCode.SUCCESS, data);
    }

    public ResultVO(boolean success, ResultCode resultCode, T data) {
        code = resultCode.getCode();
        msg = resultCode.getMsg();
        this.success = success;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
