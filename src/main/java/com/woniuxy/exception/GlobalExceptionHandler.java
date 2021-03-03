package com.woniuxy.exception;

import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//返回json格式
public class GlobalExceptionHandler{

    //空指针异常
//    @ExceptionHandler({NullPointerException.class})
//    public Result handlerNullPointException(){
//        return new Result(false, StatusCode.ERROR,"空指针异常",null);
//    }

    //用户不存在
    @ExceptionHandler({UnknownAccountException.class})
    public Result handlerUnknownAccountException(){
        return  new Result(false,StatusCode.UNKNOWNUSER,"用户名未注册");
    }

    //密码错误
    @ExceptionHandler({IncorrectCredentialsException.class})
    public Result handlerIncorrectCredentialsException(){
        return  new Result(false,StatusCode.LOGINERROR,"密码错误");
    }


    //全局大异常，兜底用
//    @ExceptionHandler({Exception.class})
//    public Result handlerException(){
//        return new Result(false, StatusCode.ERROR,"服务器异常",null);
//    }
}