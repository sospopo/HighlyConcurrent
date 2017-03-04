package com.chunmiao.seckill.service.impl;

import com.chunmiao.seckill.dao.SeckillDao;
import com.chunmiao.seckill.dao.SuccessKilledDao;
import com.chunmiao.seckill.dao.cache.RedisDao;
import com.chunmiao.seckill.dto.Exposer;
import com.chunmiao.seckill.dto.SeckillExecution;
import com.chunmiao.seckill.entity.Seckill;
import com.chunmiao.seckill.entity.SuccessKilled;
import com.chunmiao.seckill.enums.SeckillStateEnum;
import com.chunmiao.seckill.exception.RepeatKillException;
import com.chunmiao.seckill.exception.SeckillCloseException;
import com.chunmiao.seckill.exception.SeckillException;
import com.chunmiao.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by chunmiao on 17-3-3.
 */
@Service
public class SeckillServiceImpl implements SeckillService{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    //md5盐值
    private final String slat = "23r5fw4rf78!934v988&*&*AS&*78789d8i3";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.quertAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        //访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null){
                return new Exposer(false,seckillId);
            }else {
                redisDao.putSeckill(seckill);
            }
        }
        Date  startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if(nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程，不可逆转
        String md5 = getMD5(seckillId);//TODO
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    public SeckillExecution exexuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite!");
        }

        //执行秒杀逻辑:减库存，记录购买行为
        Date nowTime = new Date();

        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated!");
            } else {

             //热点商品竞争
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新到操作,rollback
                throw new SeckillCloseException("seckill is closed!");
            } else {
                //秒杀成功,commit
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            }
            }

        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch(Exception e){
                logger.error(e.getMessage(),e);
                //所有编译异常转换为运行异常
                throw new SeckillException("seckill inner error:" + e.getMessage());
            }
    }

    @Override
    public SeckillExecution exexuteSeckillProduce(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        //执行完毕后，result被赋值
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
