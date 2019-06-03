package com.phantom.seckill.service;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.entity.OrderInfo;
import com.phantom.seckill.exception.EmptyGoodsListException;
import com.phantom.seckill.exception.GlobalException;
import com.phantom.seckill.mapper.SeckillGoodsMapper;
import com.phantom.seckill.utils.UUIDUtil;
import com.phantom.seckill.vo.GoodsDetailVo;
import com.phantom.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class SeckillGoodsService {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisService redisService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 秒杀商品接口，首先减少库存，成功后生成订单，失败则表示商品库存不足，设置秒杀商品结束。
     *
     * @param goods  商品
     * @param userId 用户编号
     * @return 下单成功则返回订单信息，否则返回 null
     * @throws DataAccessException 数据库异常，将重复下单产生的异常交给上层处理
     */
    @Transactional
    public OrderInfo seckill(GoodsVo goods, Long userId) throws DataAccessException {
        Long goodsId = goods.getId();
        boolean success = goodsService.reduceStock(goodsId);
        System.out.println("success:"+success);
        if (success) {
            return seckillOrderService.createSeckillOrder(goods, userId);
        } else {
            redisService.setSeckillFailed(goodsId,userId);
            return null;
        }
    }

    /**
     * 生成秒杀入口路径。
     * 先检查商品 id 是否存在，不存在则抛出异常；
     *
     * @param goodsId 商品 id
     * @return 秒杀入口路径
     */
    public String createSeckillPath(Long goodsId) {
        if (!redisService.checkSeckillGoods(goodsId))
            throw new GlobalException("秒杀失败，不存在该商品！");
        String path = UUIDUtil.getRandomPath();
       redisService.setSeckillPath(path, goodsId);
        return path;
    }

    public List<GoodsVo> listSeckillGoods() {
        return seckillGoodsMapper.selectSeckillGoods();
    }

    // 检查路径是否存在
    public boolean checkPath(String path, Long goodsId) {
        try {
            String goodsIdStr = redisService.getSeckillGoodsId(path);
            System.out.println("goodsIdStr:"+goodsIdStr);
            return goodsId.equals(Long.valueOf(goodsIdStr));
        }catch (Exception e) {
            throw new GlobalException("非法路径！！！");
        }


    }

    // 检查当前是否是秒杀活动时间
    public boolean checkTime(GoodsVo goods, long miusaTime) {
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        return start <= miusaTime && miusaTime <= end;
    }



    public GoodsDetailVo getGoodsDetailVOByGoodsId(Long goodsId) {
        GoodsVo goods = seckillGoodsMapper.selectSeckillGoodsByGoodsId(goodsId);
        GoodsDetailVo goodsDetailVO = new GoodsDetailVo();
        goodsDetailVO.setGoods(goods);
        goodsDetailVO.setUser(hostHolder.getUser());
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long remainSeconds;
        if (now >= end) {
            remainSeconds = -1;
        } else if (now <= start) {
            remainSeconds = (start - now) / 1000;
        } else {
            remainSeconds = 0;
        }
        goodsDetailVO.setRemainSeconds(remainSeconds);
        return goodsDetailVO;
    }

    public GoodsVo getGoodsVOById(Long goodsId) {
        return seckillGoodsMapper.selectSeckillGoodsByGoodsId(goodsId);
    }


    // 初始化，将商品库存加载入内存
    public  void   seckillGoodsSet() throws EmptyGoodsListException {
        List<GoodsVo> goodsList = listSeckillGoods();
        if (goodsList == null)
            throw new EmptyGoodsListException("无任何商品可载入");
        for (GoodsVo goods : goodsList) {
            Long goodsId = goods.getId();
            Integer stockCount = goods.getStockCount();
            redisService.preloadStock(goodsId, stockCount);
        }
    }
}
