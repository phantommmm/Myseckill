package com.phantom.seckill.exception;

import com.phantom.seckill.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Result handleGlobalException(GlobalException ge) {
        return Result.loginError(ge.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException be) {
        ObjectError error = be.getAllErrors().get(0);
        return Result.loginError(error.getDefaultMessage());
    }

    @ExceptionHandler(EmptyGoodsListException.class)
    public void handleEmptyGoodsListException(EmptyGoodsListException ege) {
        System.out.println(ege.getMessage());
        log.error(ege.getMessage());
    }
}
