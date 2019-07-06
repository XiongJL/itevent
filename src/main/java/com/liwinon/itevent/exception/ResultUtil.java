package com.liwinon.itevent.exception;

/**
 *  用于生成Result.java
 */
public class ResultUtil {
    public static ExceptionRes error(int code,String msg) {
        ExceptionRes result = new ExceptionRes();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static ExceptionRes success(Object obj) {
        ExceptionRes result = new ExceptionRes();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(obj);
        return result;
    }

    public static ExceptionRes success() {
        ExceptionRes result = new ExceptionRes();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(null);
        return result;
    }


}
