package com.phantom.seckill.service;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.OrderResult;

import com.phantom.seckill.entity.OrderInfo;
import com.phantom.seckill.entity.SeckillOrder;

import com.phantom.seckill.mapper.SeckillOrderMapper;
import com.phantom.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HostHolder hostHolder;

    @Transactional
    public OrderInfo createSeckillOrder(GoodsVo goods, Long userId) throws DataAccessException {
        Long goodsId = goods.getId();
        // 生成订单
        OrderInfo orderInfo = orderInfoService.createOrder(goods, userId);
        // 对于秒杀商品，再额外生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsId);
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(userId);
        seckillOrderMapper.insertSelective(seckillOrder);
        // 将秒杀订单存入缓存
        redisService.setSeckillOrder(goodsId, seckillOrder);
        return orderInfo;
    }

    public long getSeckillResult(Long goodsId) {
        // 从缓存中取出秒杀订单
        SeckillOrder seckillOrder = redisService.getSeckillOrder(goodsId);
        long result;
        if (seckillOrder != null) { // 秒杀成功返回订单 ID
            result = seckillOrder.getOrderId();
        } else { // 秒杀订单未生成，判断秒杀是否失败
            Long userId = hostHolder.getUser().getId();
            if (redisService.isSeckillFailed(goodsId, userId))
                result = OrderResult.FAILED.getValue(); // 秒杀失败
            else
                result = OrderResult.WAITING.getValue();// 仍在处理，继续等待
        }
        return result;
    }

    public SeckillOrder getSeckillOrderByGoodsId(Long goodsId) {
        return redisService.getSeckillOrder(goodsId);
    }
}
