package com.phantom.seckill;

import com.phantom.seckill.controller.SeckillController;
import com.phantom.seckill.exception.EmptyGoodsListException;
import com.phantom.seckill.vo.GoodsVo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@MapperScan("com.phantom.seckill.mapper")
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }



}
