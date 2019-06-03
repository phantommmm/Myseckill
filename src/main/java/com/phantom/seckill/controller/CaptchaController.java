package com.phantom.seckill.controller;


import com.google.code.kaptcha.impl.DefaultKaptcha;

import com.phantom.seckill.common.access.AccessLimit;
import com.phantom.seckill.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


/**
 * 图片验证码
 */
@Controller
@RequestMapping("/code")
public class CaptchaController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisService redisService;

    /**
     * 生成验证码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */


    @GetMapping("/defaultKaptcha/{goodsId}")
    @AccessLimit(seconds = 10, maxCount = 5)
    @ResponseBody
    public void defaultKaptcha(@PathVariable Long goodsId, HttpServletRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        String rightCode = "";
        System.out.println("goodsId:" + goodsId);
        try {
            // 生产验证码字符串并保存到redis中
            rightCode = (String) defaultKaptcha.createText();
            redisService.setCode(rightCode, goodsId);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(rightCode);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();

    }


    /**
     * 3、校对验证码
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @RequestMapping("/imgvrifyControllerDefaultKaptcha")
    @AccessLimit(seconds = 10, maxCount = 5)
    @ResponseBody
    public Boolean imgvrifyControllerDefaultKaptcha(@RequestParam Long goodsId, @RequestParam String tryCode, HttpServletRequest httpServletRequest) {
        String rightCode = redisService.getCode(goodsId);
        if (!rightCode.equals(tryCode)) {
            redisService.delCode(goodsId);
            return false;
        } else {
            return true;
        }

    }

}