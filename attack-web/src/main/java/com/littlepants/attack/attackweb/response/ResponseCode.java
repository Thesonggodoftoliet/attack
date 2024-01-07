package com.littlepants.attack.attackweb.response;

public enum ResponseCode {
    SUCCESS(200, "请求成功"),
    WARN(500, "网络异常"),
    NOTFOUND(404,"资源不存在"),
    NOT_QUALIFIED(403,"权限不够")
    ;

    private final int code;
    private final String msg;

    ResponseCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
