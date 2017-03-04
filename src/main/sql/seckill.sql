--秒杀执行存储过程
DELIMITER $$ --;转化为$$
--定义存储过程
--参数：in 表示输入参数;out 输出canshu
-- row_count():返回上一条修改类型的sql(delete,insert,update)的影响函数
--row_count():0:未修改数据，>0修改函数 <0 sql错误
CREATE PROCEDURE `seckill`.`execute_seckill`
  (IN v_seckill_id bigint,IN v_phone bigint,
  IN v_kill_time TIMESTAMP ,out r_result INT)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION;
    INSERT ignore into success_killed
      (seckill_id,user_phone,create_time)
      VALUES (v_seckill_id,v_phone,v_kill_time);
     SELECT ROW_COUNT () INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill
      SET  number = number - 1
      WHERE seckill_id = v_seckill_id
        and end_time > v_kill_time
        and start_time < v_kill_time
        and number > 0;

      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count = 0) THEN
        set r_result=0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        set r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
END;
$$--存储过程结束

DELIMITER ;
set @r_result=-3;
--执行存储过程
call execute_seckill(1003,13502178891,now(),@r_result);

--获取结果
SELECT @r_result;

--1：存储过程优化：事物行锁持有时间爱呢
--2：不要过度依赖存储过程
--3.简单逻辑依赖存储过程
--4。：QPS：一个秒杀行为可接近6000/gps