package com.example.demo.test.ThreadTest.dao;

import com.alibaba.fastjson.JSON;
import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;

/***
 * 子线程任务整体提交/回滚事务，单个子线程中若干数据一致性
 *  例如：5个线程，每个线程10条数据，如果某个线程中的数据出现异常，则回滚当前线程中的10条所有数据
 */
@Slf4j
public class OrderThreadTransaction implements Runnable {

    private OrderDao orderDao;

    private List<OrderDto> orderDtoList;

    public OrderThreadTransaction(OrderDao orderDao, List<OrderDto> orderDtoList){
        this.orderDao = orderDao;
        this.orderDtoList = orderDtoList;
    }

    @Override
    public void run() {
        log.info("当前线程{}"+Thread.currentThread().getName()+"处理数据集合{}"+ JSON.toJSONString(orderDtoList));

        //将事务状态都放在同一个事务里面
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();

        //事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        //构建上下文
        PlatformTransactionManager txManager = SpringContextUtil.getBean(PlatformTransactionManager.class);

        //获得事务状态
        TransactionStatus status = txManager.getTransaction(def);

        try {
            //DB处理
            for (OrderDto dto : orderDtoList) {
                dto.setOrderStatus("2");
                orderDao.save(dto);
                //模拟发生异常，抛出异常
                if (dto.getOrderNo().equals("20230112164008119")){
                    System.out.println(1/0);
                }
            }
        } catch (Exception e) {
            //回滚事务
            txManager.rollback(status);
            throw e;
        }
        //提交事务
        txManager.commit(status);
    }
}