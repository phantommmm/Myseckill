package com.phantom.seckill.controller;

import com.phantom.seckill.common.Result;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.service.RedisService;
import com.phantom.seckill.service.UserService;
import com.phantom.seckill.utils.UUIDUtil;
import com.phantom.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/post_login")
    @ResponseBody
    public Result postLogin(@Valid LoginVo loginVo, HttpServletResponse response) {

        User user = userService.login(Long.valueOf(loginVo.getPhone()),loginVo.getPassword());
        Cookie cookie = setToken(user);
        response.addCookie(cookie);
        System.out.println("postlogin");
        return Result.success("登录成功!");
    }

    private Cookie setToken(User user) {
        String token = UUIDUtil.getToken();
        redisService.setToken(user, token);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(2592000);// 2592000 = 60*60*24*30 = 30 days
        cookie.setPath("/");//在同一服务器内共享cookie
        return cookie;
    }

}
