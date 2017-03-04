# HighlyConcurrent
#这是一个高并发秒杀的实例网站，主要用于测试ssm框架+redis+mysql的高压测试
#开发环境：IDEA
#开发语言：JAVA
#使用框架：SpringMVC+Spring+MyBatis
#使用数据库：Mysql
#使用缓存：redis
#经测试其中单项并发秒杀可以达到一个秒杀行为可接近2000/gps，其中对数据库进行了优化，在数据库中创建事物来减少网络延迟。从而减少行锁，达到提高并发秒杀数量
