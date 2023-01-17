package com.example.demo.test.ThreadTest.service.Impl;

import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.ThreadTest.service.ThreadHandleTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 主线程和子线程任务整体提交/回滚事务，多线程整体数据一致性
 *  例如：5个线程，每个线程10条数据，如果某个线程中的数据出现异常，则回滚主子线程中的50条所有数据
 */
@Slf4j
@Service
public class ThreadHandleTransactionServiceImpl implements ThreadHandleTransactionService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /***
     * 多线程事务控制
     * @param threadData
     * @param threadLatch
     * @param mainLatch
     * @param isError
     */
    @Override
    public void ThreadHandleTransaction(List<OrderDto> threadData, CountDownLatch threadLatch, CountDownLatch mainLatch, AtomicBoolean isError) {
        //将事务状态都放在同一个事务里面
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        //事物隔离级别，开启新事务，安全起见
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        //获得事务状态
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        log.info("子线程{}" + Thread.currentThread().getName() + "处理数据量{}" + threadData.size());
        try {
            //DB处理
            for (OrderDto dto : threadData) {
                dto.setOrderStatus("2");
                orderDao.save(dto);
                //模拟发生异常
                if (dto.getOrderNo().equals("20230112164008119")){
                    //System.out.println(1/0);
                }
            }
        } catch (Throwable e) {
            log.error("多线程事务控制出现异常{}"+e.getMessage());
            isError.set(true);
        } finally {
            //切换到主线程执行
            threadLatch.countDown();
        }

        try {
            //等待主线程执行
            mainLatch.await();
        } catch (Throwable e) {
            isError.set(true);
        }

        //判断子线程任务是否有错误
        if (isError.get()) {
            transactionManager.rollback(transactionStatus);
        } else {
            transactionManager.commit(transactionStatus);
        }
    }
}