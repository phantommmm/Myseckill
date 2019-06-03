package com.phantom.seckill.service;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.OrderStatus;
import com.phantom.seckill.entity.OrderInfo;
import com.phantom.seckill.mapper.OrderInfoMapper;
import com.phantom.seckill.vo.GoodsVo;
import com.phantom.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service
public class OrderInfoService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public List<OrderDetailVo> getOrderDetail(Long userId) {
        return orderInfoMapper.selectOrderDetailByOrderId(userId);
    }

    public OrderInfo createOrder(GoodsVo goods, Long userId) {
        OrderInfo info = new OrderInfo();
        info.setCreateDate(new Date());
        info.setGoodsCount(1);
        info.setGoodsId(goods.getId());
        info.setGoodsName(goods.getGoodsName());
        info.setGoodsPrice(goods.getMiusaPrice().doubleValue());
        info.setOrderChannel(1);
        info.setUserId(userId);
        info.setStatus(OrderStatus.UNPAID.ordinal());
        orderInfoMapper.insertSelective(info);
        return info;
    }
}
