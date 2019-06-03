package com.phantom.seckill.service;

import com.phantom.seckill.SeckillApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes= SeckillApplication.class)
public class SeckillGoodsServiceTest {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Test
    public void seckillGoodsSet() {
        seckillGoodsService.seckillGoodsSet();
    }
}