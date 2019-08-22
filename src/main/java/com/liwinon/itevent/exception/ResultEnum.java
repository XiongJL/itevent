package com.liwinon.itevent.exception;

public enum ResultEnum {
    UNKNOWN_ERROR(500,"未知错误"),
    SUCCESS(200,"操作成功"),
    ERROR_1(111,"请重新登录"),
    ERROR_2(112,"用户不存在,请重新登录"),
    ERROR_3(401,"用户或密码错误"),
    ERROR_4(113,"用户不存在"),
    ERROR_5(114,"token已过期"),
    ERROR_6(115,"该申请事件不存在")
    ;

    private int code;
    private String msg;

    ResultEnum(int code, String msg) {
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
