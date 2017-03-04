package com.chunmiao.seckill.service;

import com.chunmiao.seckill.dto.Exposer;
import com.chunmiao.seckill.dto.SeckillExecution;
import com.chunmiao.seckill.entity.Seckill;
import com.chunmiao.seckill.exception.RepeatKillException;
import com.chunmiao.seckill.exception.SeckillCloseException;
import com.chunmiao.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by chunmiao on 17-3-3.
 */
//站在使用者角度设计接口
    //方法粒度，参数，返回类型(return 类型/异常)
public interface SeckillService {
    //查询所有秒杀记录
    List<Seckill> getSeckillList();
    //查询单个秒杀记录
    Seckill getById(long seckillId);
    //秒杀开始时，输出秒杀接口的地址，否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);
    //执行秒杀操作
    SeckillExecution exexuteSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException,RepeatKillException,SeckillCloseException;

    SeckillExecution exexuteSeckillProduce(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

}
