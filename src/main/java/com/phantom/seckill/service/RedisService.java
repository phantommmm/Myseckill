package com.phantom.seckill.service;

import com.alibaba.fastjson.JSON;
import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.RedisAdapter;

import com.phantom.seckill.entity.SeckillOrder;
import com.phantom.seckill.entity.User;
import com.phantom.seckill.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RedisService {
    @Autowired
    private RedisAdapter redisAdapter;
    @Autowired
    private HostHolder hostHolder;

    public Long accessCount(Long userId, String url, int seconds) {
        String key = RedisKeyUtil.accessKey(userId, url);
        Long count = redisAdapter.incr(key);
        redisAdapter.expire(key, seconds);//设置Key的过期时间
        return count;
    }

    /**
     * 检查秒杀商品是否存在。
     *
     * @param goodsId 商品 id
     * @return 存在则返回true，否则返回 false。
     */
    public Boolean checkSeckillGoods(Long goodsId) {
        String key = RedisKeyUtil.stockKey(goodsId);
        return redisAdapter.exists(key);
    }

    public String preloadStock(Long goodsId, Integer stockCount) {
        String key = RedisKeyUtil.stockKey(goodsId);
        return redisAdapter.set(key, stockCount + "");
    }

    public Long preReduceStock(Long goodsId) {
        String key = RedisKeyUtil.stockKey(goodsId);
        return redisAdapter.decr(key);
    }

    public String setToken(User user, String token) {
        String key = RedisKeyUtil.tokenKey(token);
        return redisAdapter.set(key, JSON.toJSONString(user));
    }

    public String setCode(String code,Long goodsId){
        User currentUser=hostHolder.getUser();
        String key=RedisKeyUtil.codeKey(currentUser.getId(),goodsId);
        return redisAdapter.set(key,code);
    }

    public String setSeckillPath(String path, Long goodsId) {
            User currentUser = hostHolder.getUser();
            String key = RedisKeyUtil.MiusaPathKey(path,currentUser.getId());
            return redisAdapter.setex(key, goodsId + "", 600);
    }

    public void setSeckillFailed(Long goodsId, Long userId) {
        String key = RedisKeyUtil.miusaFailedKey(goodsId);
        redisAdapter.sadd(key, userId + "");
    }

    public boolean isSeckillFailed(Long goodsId, Long userId) {
        String key = RedisKeyUtil.miusaFailedKey(goodsId);
        return redisAdapter.sismember(key, userId + "");
    }

    public void setSeckillOrder(Long goodsId, SeckillOrder seckillOrder) {
        String key = RedisKeyUtil.miusaOrderKey(seckillOrder.getUserId(), goodsId);
        redisAdapter.set(key, JSON.toJSONString(seckillOrder));
    }

    public SeckillOrder getSeckillOrder(Long goodsId) {
        User currentUser = hostHolder.getUser();
        String key = RedisKeyUtil.miusaOrderKey(currentUser.getId(), goodsId);
        return JSON.parseObject(redisAdapter.get(key), SeckillOrder.class);
    }

    public String getCode(Long goodsId){
        User currentUser=hostHolder.getUser();
        String key=RedisKeyUtil.codeKey(currentUser.getId(),goodsId);
        return redisAdapter.get(key);
    }

    public Long delCode(Long goodsId){
        User currentUser=hostHolder.getUser();
        String key=RedisKeyUtil.codeKey(currentUser.getId(),goodsId);
        return redisAdapter.del(key);
    }


    public String getSeckillGoodsId(String path) {
        User currentUser = hostHolder.getUser();
        String key = RedisKeyUtil.MiusaPathKey(path, currentUser.getId());
        return redisAdapter.get(key);
    }

    public String getHtml(String pageName) {
        return redisAdapter.get(pageName);
    }

    public String setHtml(String pageName, String page, int seconds) {
        return redisAdapter.setex(pageName, page, seconds);
    }
}
