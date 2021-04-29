package com.yww.handler;

import com.yww.Result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *     统一异常处理类
 * </p>
 *
 * @ClassName GlobalExceptionHandler
 * @Author yww
 * @Date 2021/3/2 9:49
 * @Version 1.0
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().message("执行全局异常处理！");
    }

    @ExceptionHandler(ProjectException.class)
    @ResponseBody
    public Result error(ProjectException e) {
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }

}
