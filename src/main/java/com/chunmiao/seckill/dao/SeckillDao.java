package com.chunmiao.seckill.dao;

import com.chunmiao.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chunmiao on 17-3-1.
 */
public interface SeckillDao {
    //减库存
    int reduceNumber(@Param("seckillId") Long seckillId, @Param("killTime") Date killTime);
    //根据id查找
    Seckill queryById(Long seckillId);
    //根据偏移量查询秒杀商品列表
    List<Seckill> quertAll(@Param("offset") int offset,@Param("limit") int limit);

    void killByProcedure(Map<String,Object> paramMap);

}
