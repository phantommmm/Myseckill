package com.phantom.seckill.Interceptor;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.Result;
import com.phantom.seckill.common.access.AccessLimit;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.service.RedisService;
import com.phantom.seckill.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisService redisService;

    /**
     * 拦截器方法 限制访问次数
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果不是处理器方法直接返回
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //获得注解
        AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
        if (accessLimit == null) return true;
        //获得各个字段值
        int maxCount = accessLimit.maxCount();
        int seconds = accessLimit.seconds();
        String url = request.getRequestURI();
        //保存当前用户的访问次数 时间
        User user = hostHolder.getUser();
        Long count = redisService.accessCount(user.getId(), url, seconds);
        //一定时间内访问次数过多
        if (count > maxCount) {
            WebUtil.modifyResponse(response, Result.accessError("访问频繁 请稍后再试"));
            return false;
        }
        return true;

    }
}
