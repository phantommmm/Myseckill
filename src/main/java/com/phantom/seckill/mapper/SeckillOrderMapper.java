package com.phantom.seckill.mapper;

import com.phantom.seckill.entity.SeckillOrder;
import com.phantom.seckill.entity.SeckillOrderExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SeckillOrderMapper {
    long countByExample(SeckillOrderExample example);

    int deleteByExample(SeckillOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SeckillOrder record);

    int insertSelective(SeckillOrder record);

    int insertOrder(SeckillOrder record);

    List<SeckillOrder> selectByExample(SeckillOrderExample example);

    SeckillOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SeckillOrder record, @Param("example") SeckillOrderExample example);

    int updateByExample(@Param("record") SeckillOrder record, @Param("example") SeckillOrderExample example);

    int updateByPrimaryKeySelective(SeckillOrder record);

    int updateByPrimaryKey(SeckillOrder record);


}