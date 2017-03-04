package com.chunmiao.seckill.enums;

import com.chunmiao.seckill.entity.Seckill;

/**
 * Created by chunmiao on 17-3-3.
 */
public enum SeckillStateEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;
    private String staeInfo;

    SeckillStateEnum(int state, String staeInfo) {
        this.state = state;
        this.staeInfo = staeInfo;
    }

    public int getState() {
        return state;
    }

    public String getStaeInfo() {
        return staeInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for(SeckillStateEnum state : values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }

}
