package com.phantom.seckill.vo;


import com.phantom.seckill.entity.User;

public class GoodsDetailVo {
    private long remainSeconds;
    private GoodsVo goods;
    private User user;

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
