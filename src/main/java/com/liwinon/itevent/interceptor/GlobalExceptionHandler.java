package com.liwinon.itevent.interceptor;

import com.liwinon.itevent.exception.MyException;
import com.liwinon.itevent.exception.ResultEnum;
import com.liwinon.itevent.exception.ResultUtil;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        if (e instanceof MyException){
            MyException my = (MyException)e;
            return ResultUtil.error(my.getCode(),my.getMessage());
        }else{
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }
}
