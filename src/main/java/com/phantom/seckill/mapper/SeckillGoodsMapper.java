package com.phantom.seckill.mapper;


import com.phantom.seckill.entity.SeckillGoods;
import com.phantom.seckill.entity.SeckillGoodsExample;
import com.phantom.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SeckillGoodsMapper {

    List<GoodsVo> selectSeckillGoods();

    GoodsVo selectSeckillGoodsByGoodsId(Long goodsId);

    SeckillGoods verifySeckillGoodsByGoodsId(Long goodsId);

    long countByExample(SeckillGoodsExample example);

    int deleteByExample(SeckillGoodsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    List<SeckillGoods> selectByExample(SeckillGoodsExample example);

    SeckillGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SeckillGoods record, @Param("example") SeckillGoodsExample example);

    int updateByExample(@Param("record") SeckillGoods record, @Param("example") SeckillGoodsExample example);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);

}