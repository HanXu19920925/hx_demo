package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/***
 * redis分布式锁
 * 核心redis命令如下：
 * SETNX key value
 * GETSET key value
 * setnx key value setnx就是，如果没有这个key，那么就set一个key-value, 但是如果这个key已经存在，那么将不会再次设置，get出来的value还是最开始set进去的那个value
 * getset key value 先通过key获取value，然后再将新的value set进去
 * 为什么要有避免死锁的一步呢？
 * 假设没有『避免死锁』这一步，结果在执行过程中出了问题，在操作数据库、网络、io的时候抛了个异常，这个异常是偶然抛出来的，就那么偶尔一次，那么会导致解锁步骤不去执行，这时候就没有解锁，后面的请求进来自然也或得不到锁，这就被称之为死锁
 * 而这里的『避免死锁』，就是给锁加了一个过期时间，如果锁超时了，就返回true，解开之前的那个死锁
 */
@Component
public class RedisUtil {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key, String value) {
        //这个其实就是setnx命令，只不过在java这边稍有变化，返回的是boolean
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //避免死锁，且只让一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期了
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValues = redisTemplate.opsForValue().getAndSet(key, value);
            //只会让一个线程拿到锁，如果旧的value和currentValue相等，只会有一个线程达成条件，因为第二个线程拿到的oldValues已经和currentValue不一样了
            if (!StringUtils.isEmpty(oldValues) && oldValues.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value 当前时间+超时时间
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            logger.error("『redis分布式锁』解锁异常，{}", e);
        }
    }
}