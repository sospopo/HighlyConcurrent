package com.chunmiao.seckill.dao;

import com.chunmiao.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by chunmiao on 17-3-3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001L;
        long phone = 18040536556L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount = " + insertCount);


    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1001L,18040536556L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }

}