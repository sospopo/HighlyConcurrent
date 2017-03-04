package com.chunmiao.seckill.exception;

/**
 * Created by chunmiao on 17-3-3.
 */
//秒杀相关业务异常
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
