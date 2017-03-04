package com.chunmiao.seckill.dao;

import com.chunmiao.seckill.entity.Seckill;
import com.chunmiao.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chunmiao on 17-3-1.
 */
public interface SuccessKilledDao {
    //插入购买明细,可过滤重复
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    //根据id查询SuccessKilled并携带秒杀场景对象实体
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
