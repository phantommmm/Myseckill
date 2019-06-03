package com.phantom.seckill.service;

import com.phantom.seckill.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public boolean reduceStock(Long goodsId) {
        return goodsMapper.reduceStock(goodsId) > 0;
    }

}
