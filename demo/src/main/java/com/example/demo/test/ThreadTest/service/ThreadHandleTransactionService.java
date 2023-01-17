package com.example.demo.test.ThreadTest.service;

import com.example.demo.test.MallTest.pojo.OrderDto;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public interface ThreadHandleTransactionService {

    /***
     * 多线程事务控制
     * @param threadData
     * @param threadLatch
     * @param mainLatch
     * @param isError
     */
    void ThreadHandleTransaction(List<OrderDto> threadData, CountDownLatch threadLatch, CountDownLatch mainLatch, AtomicBoolean isError);
}