package com.chunmiao.seckill.exception;

/**
 * Created by chunmiao on 17-3-3.
 */
//重复秒杀异常(运行期异常)
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
