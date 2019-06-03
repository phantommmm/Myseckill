package com.phantom.seckill.controller;

import com.phantom.seckill.common.Result;
import com.phantom.seckill.service.UserService;
import com.phantom.seckill.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;


    @GetMapping("register")
    public String getRegister(){
        return "register";
    }

    @PostMapping("post_register")
    @ResponseBody
    public Result PostRegister(@Valid RegisterVo registerVo, HttpServletResponse response){
      boolean success= userService.registerUser(Long.valueOf(registerVo.getPhone()),registerVo.getNickname(),registerVo.getPassword());
        if(success){
            return Result.success("注册成功！");
        }else {
            return Result.register("该手机号已经注册！");
        }
    }


}
