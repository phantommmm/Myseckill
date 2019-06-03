package com.phantom.seckill.controller;

import com.phantom.seckill.common.HostHolder;
import com.phantom.seckill.common.OrderResult;
import com.phantom.seckill.common.Result;
import com.phantom.seckill.common.access.AccessLimit;
import com.phantom.seckill.exception.EmptyGoodsListException;
import com.phantom.seckill.rabbitmq.MsgSender;
import com.phantom.seckill.rabbitmq.SeckillMessage;
import com.phantom.seckill.service.RedisService;
import com.phantom.seckill.service.SeckillGoodsService;
import com.phantom.seckill.service.SeckillOrderService;
import com.phantom.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MsgSender msgSender;

    // 内存标记商品售罄列表
    private CopyOnWriteArraySet<Long> seckillGoodsSoldOut = new CopyOnWriteArraySet<>();






    /**
     * 获取秒杀路径
     * @param goodsId
     * @return
     */
    @GetMapping("/path")
    @AccessLimit(seconds = 10,maxCount = 5)
    @ResponseBody
    public Result getSeckillPath(@RequestParam Long goodsId){
        String path=seckillGoodsService.createSeckillPath(goodsId);
        System.out.println("path:"+path);
        return Result.success(path);
    }

    /**
     * 执行秒杀
     * @param goodsId
     * @param path
     * @param response
     * @return
     */
    @PostMapping("/{path}/executeSeckill")
    @ResponseBody
    public Result executeSeckill(@RequestParam Long goodsId, @PathVariable String path, HttpServletResponse response){
        System.out.println("miaosha");
        boolean isPath=seckillGoodsService.checkPath(path,goodsId);
        //判断秒杀路径是否匹配
        if(!isPath){
            return Result.error("非法路径!");
        }
        //判断是否下单
        if(seckillOrderService.getSeckillOrderByGoodsId(goodsId)!=null){
            return Result.orderError("您已下单，请前往查看");
        }
        //从内存中查看商品是否售罄
        if(seckillGoodsSoldOut.contains(goodsId)){
            return Result.goodsError("商品已售罄,下次请早");
        }
        // 访问 redis，预减库存
        Long count=redisService.preReduceStock(goodsId);
        if(count<0){
            seckillGoodsSoldOut.add(goodsId);
            return Result.goodsError("商品已售罄,下次请早");
        }
        // 将订单放到消息队列异步处理，然后返回排队等候的状态
        SeckillMessage seckillMessage=getSeckillMessage(goodsId);
        msgSender.sendSeckillMessage(seckillMessage);
        return Result.success(OrderResult.WAITING.getValue());

    }

    /**
     * 获取秒杀结果
     * @param goodsId
     * @return 下单成功返回订单编号，失败则：-1 表示下单失败，0 表示排队中
     */
    @GetMapping("result")
    @ResponseBody
    public Result getSeckillResult(@RequestParam Long goodsId){
        long result=seckillOrderService.getSeckillResult(goodsId);
            return Result.success(result);
    }

    /*
        封装seckillMessage
     */
    private SeckillMessage getSeckillMessage(Long goodsId){
            Long id=hostHolder.getUser().getId();
            Long current= System.currentTimeMillis();
            SeckillMessage seckillMessage=new SeckillMessage();
            seckillMessage.setGoodsId(goodsId);
            seckillMessage.setUserId(id);
            seckillMessage.setSeckillTime(current);
            return seckillMessage;
    }
}
