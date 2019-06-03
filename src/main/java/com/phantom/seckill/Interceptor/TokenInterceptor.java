package com.phantom.seckill.Interceptor;

import com.alibaba.fastjson.JSON;
import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.RedisAdapter;
import com.phantom.seckill.common.Result;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.utils.RedisKeyUtil;
import com.phantom.seckill.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisAdapter redisAdapter;
    @Autowired
    private HostHolder hostHolder;

    /**
     * 获取Token 验证
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = null;

        //获取cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        //token 为null 则未登录 回到登录页面
        if (token == null) {
            System.out.println("token==null");
            response.sendRedirect("/login");
            return false;
        }
        User user = null;
        if (!StringUtils.isEmpty(token)) {
            //包装成Key字符串
            String key = RedisKeyUtil.tokenKey(token);
            //转成user
            user = JSON.parseObject(redisAdapter.get(key), User.class);
            //user==null 重新登录
            if (user == null) {
                response.sendRedirect("/login");
                return false;
            }
        }

        //设置 共享USER
        hostHolder.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}

