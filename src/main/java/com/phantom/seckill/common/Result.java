package com.phantom.seckill.common;

import lombok.Getter;

/**
 * 结果类 成功:传数据 失败:传错误信息
 */
@Getter
public class Result {
    private int code;//状态码

    private String message;//信息

    private Object data;//数据

    /*
         成功构造器
     */
    public  Result(int code,String message,Object data){
        this.code=code;
        this.data=data;
        this.message=message;
    }

    /*
        失败构造器
     */
    public  Result(int code,String message){
        this.code=code;
        this.message=message;
    }


    public static Result success(Object data){
        return new Result(0,"SUCCESS",data);
    }
    public static Result register(String message){return new Result(-7,"REGISTER ERROR:"+message);}
    public static Result error(String message){
        return new Result(-1,"ERROR:"+message);
    }
    public static Result loginError(String message) {
        return new Result(-2, "LOGIN ERROR:" + message);
    }
    public static Result serverError(String message){
        return new Result(-3,"SERVER ERROR:"+message);
    }
    public static Result goodsError(String message){
        return new Result(-4,"GOODS ERROR:"+message);
    }
    public static Result orderError(String message){
        return new Result(-5,"ORDER ERROR:"+message);
    }
    public static Result accessError(String message){
        return new Result(-6,"ACCESS ERROR:"+message);
    }
}
