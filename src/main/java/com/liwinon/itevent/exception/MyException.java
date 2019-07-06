package com.liwinon.itevent.exception;

public class MyException extends RuntimeException {
    private int code;

    public MyException( int code, String message ) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
