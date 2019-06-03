package com.phantom.seckill.utils;

import com.alibaba.fastjson.JSON;
import com.phantom.seckill.common.Result;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class WebUtil {

    public static void modifyResponse(HttpServletResponse response, Result result) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(result));
        writer.flush();
    }

}
