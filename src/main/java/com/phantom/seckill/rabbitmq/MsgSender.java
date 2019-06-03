package com.phantom.seckill.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MsgSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage seckillMessage) {
        String message = JSON.toJSONString(seckillMessage);
        amqpTemplate.convertAndSend(MQConfiguration.SECKILL_QUEUE, message);
    }
}
