package com.zq.xijue.core;

/**
 * @author zhangQ
 * @description 响应码枚举
 */
public enum ResultCode {

    SUCCESS(1000, "操作成功"),

    FAILED(1001, "响应失败"),

    VALIDATE_FAILED(1002, "参数校验失败"),

    MOBILE_TIMES_LIMIT(1003, "短信发送频繁，请稍后再试"),

    NOT_VIP(2001, "开通VIP，下载全站资源~"),

    VIP_EXPIRE(2002, "您的vip已过期，请续费~"),
    ERROR(5000, "未知错误");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
