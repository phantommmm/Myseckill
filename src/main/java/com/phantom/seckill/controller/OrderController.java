package com.phantom.seckill.controller;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.service.OrderInfoService;
import com.phantom.seckill.service.RedisService;
import com.phantom.seckill.vo.OrderDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @GetMapping("/detail")
    public String getOrderDetail(Model model, HttpServletResponse response, HttpServletRequest request) {
        //页面缓存
        String html;
        if ((html = redisService.getHtml("order_detail_html")) != null) {
            return html;
        }

        User user = hostHolder.getUser();
        Long userId = hostHolder.getUser().getId();
        List<OrderDetailVo> orderList = orderInfoService.getOrderDetail(userId);
        model.addAttribute("orderList", orderList);
        model.addAttribute("user", user);

        // 如果未命中，则使用 ThymeleafViewResolver 进行渲染，得到渲染后的页面。
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("order_detail", ctx);
        // 渲染成功则将页面缓存并返回。
        if (html != null)
            redisService.setHtml("order_detail_html", html, 60);


        return "order_detail";
    }
}
