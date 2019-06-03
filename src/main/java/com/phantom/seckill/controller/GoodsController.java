package com.phantom.seckill.controller;

import com.phantom.seckill.common.Result;

import com.phantom.seckill.service.RedisService;
import com.phantom.seckill.service.SeckillGoodsService;
import com.phantom.seckill.vo.GoodsDetailVo;
import com.phantom.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 获取商品列表
     */
    @GetMapping(value = "/list" ,produces = "text/html")//produces作用:设置返回值类型
    @ResponseBody
    public String getGoodsList(Model model, HttpServletResponse response, HttpServletRequest request){
        //页面缓存
        String html;
        //先从缓存中查找 有则直接返回
        if((html=redisService.getHtml("goods_list_html")) !=null){
            return html;
        }
        List<GoodsVo> goodsList = seckillGoodsService.listSeckillGoods();
        model.addAttribute("goodsList",goodsList);
        //使用 ThymeleafViewResolver 进行渲染，得到渲染后的页面。
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        //将渲染后的html存入redis
        if(html!=null){
            redisService.setHtml("goods_list_html",html,60);
        }
        return html;
    }

    /**
     * 根据商品Id获取商品详情
     * @param goodsId
     * @return
     */
    @GetMapping("/detail/{goodsId}")
    @ResponseBody
    public Result getGoodsDetail(@PathVariable Long goodsId,Model model){

        GoodsDetailVo goodsDetailVo =seckillGoodsService.getGoodsDetailVOByGoodsId(goodsId);

        return Result.success(goodsDetailVo);

    }


}
