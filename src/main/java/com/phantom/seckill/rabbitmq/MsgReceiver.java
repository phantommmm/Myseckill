package com.phantom.seckill.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.phantom.seckill.service.SeckillGoodsService;
import com.phantom.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
public class MsgReceiver {

    @Autowired
    SeckillGoodsService seckillGoodsService;

    private static AtomicInteger failedTask = new AtomicInteger();

    @RabbitListener(queues = MQConfiguration.SECKILL_QUEUE)
        public void receiveSeckillMessage(String message) {
        SeckillMessage seckillMessage = JSON.parseObject(message, SeckillMessage.class);
        Long userId = seckillMessage.getUserId();
        Long goodsId = seckillMessage.getGoodsId();
        GoodsVo goods = seckillGoodsService.getGoodsVOById(goodsId);
        // 检查是否处于秒杀时间
        long seckillTime = seckillMessage.getSeckillTime();
        boolean onTime = seckillGoodsService.checkTime(goods, seckillTime);
        if (onTime) { // 如果处于秒杀时间则进行秒杀
            try {
                seckillGoodsService.seckill(goods, userId);
            } catch (Exception e) {
                log.info("failedTask - " + failedTask.incrementAndGet());
            }
        }

    }
}
