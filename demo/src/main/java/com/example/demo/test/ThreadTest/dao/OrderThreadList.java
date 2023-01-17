package com.example.demo.test.ThreadTest.dao;

import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.ThreadTest.pojo.QueryParamDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class OrderThreadList implements Callable<List> {

    private OrderDao orderDao;

    //查询条件
    private QueryParamDto queryParamDto;

    //索引起始
    private int start;

    //索引结束
    private int end;

    private CountDownLatch countDownLatch;

    public OrderThreadList(OrderDao orderDao, QueryParamDto queryParamDto, int start, int end, CountDownLatch countDownLatch) {
        this.orderDao = orderDao;
        this.queryParamDto = queryParamDto;
        this.start = start;
        this.end = end;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public List call() throws Exception {
        List<OrderDto> getList = orderDao.getQueryData(queryParamDto.getDate(), start, end);
        countDownLatch.countDown();
        log.info("当前线程{}"+Thread.currentThread().getName()+"处理数据数量{}"+getList.size());
        return getList;
    }
}