package com.phantom.seckill.rabbitmq;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SeckillMessage {

    private Long userId;

    private Long goodsId;

    private long seckillTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public long getSeckillTime() {
        return seckillTime;
    }

    public void setSeckillTime(long seckillTime) {
        this.seckillTime = seckillTime;
    }

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "userId=" + userId +
                ", goodsId=" + goodsId +
                ", seckillTime=" + seckillTime +
                '}';
    }
}
