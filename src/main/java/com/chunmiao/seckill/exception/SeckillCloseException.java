package com.chunmiao.seckill.exception;

/**
 * Created by chunmiao on 17-3-3.
 */
//秒杀关闭异常
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
