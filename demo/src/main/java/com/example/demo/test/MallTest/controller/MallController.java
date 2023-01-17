package com.example.demo.test.MallTest.controller;

import com.example.demo.test.MallTest.service.MallService;
import com.example.demo.util.RedisUtil;
import io.swagger.annotations.Api;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * springBoot 订单库存
 * @author hanxu
 */
@RestController
@RequestMapping("/mall")
@Api(tags = "springBoot 订单库存")
public class MallController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     设置超时时间10秒
     */
    private static final int TIMEOUT = 10*1000;

    @Autowired
    private MallService mallService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/saveAndUpdate")
    public String saveAndUpdate(@RequestParam(value = "stockId") String stockId){
        //利用Redis分布式锁处理高并发-方式一
        //加锁
        Long time = System.currentTimeMillis() + TIMEOUT;
        try {
            boolean isLock = redisUtil.lock(stockId, String.valueOf(time));
            if(!isLock){
                return "当前记录正在操作中，请稍后再试";
            }
            //执行业务逻辑
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("利用Redis分布式锁处理高并发出现异常{}" + e.getMessage());
        } finally {
            //解锁
            redisUtil.unlock(stockId, String.valueOf(time));
        }

        //利用Redis分布式锁处理高并发-方式二
        String lockKey = "";//通常是接口名+当前登录用户
        RLock rlock = redissonClient.getLock(lockKey);
        try {
            //加锁
            rlock.lock();
            //执行业务逻辑
        } catch (Exception e){
            e.printStackTrace();
            logger.error("利用Redis分布式锁处理高并发出现异常{}" + e.getMessage());
        } finally {
            //解锁
            rlock.unlock();
        }
        return mallService.saveAndUpdate(stockId);
    }
}