package com.example.demo.test.AsyncTest.service.Impl;

import com.example.demo.test.AsyncTest.service.AsyncService;
import com.example.demo.test.MallTest.dao.LogDao;
import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.dao.StockDao;
import com.example.demo.test.MallTest.pojo.LogDto;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.MallTest.pojo.StockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private LogDao logDao;

    /**
     * 异步调用，无返回值
     */
    @Async("asyncTaskExecutor")
    @Override
    public void asyncTask(String str) {
        log.info("message{} " + str);
        try {
            //模拟耗时
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("异步调用，无返回值出现异常{}" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 异步调用，有返回值，Feature处理简单异步任务
     */
    @Async("asyncTaskExecutor")
    @Override
    public Future<String> asyncTaskForFuture() {
        log.info("asyncTaskExecutor{}");
        List<StockDto> stockList = stockDao.findAll();
        List<Integer> intList = stockList.stream().map(StockDto::getStockNum).collect(Collectors.toList());
        return AsyncResult.forValue(intList.toString());
    }

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<List<OrderDto>> orderList() {
        log.info("orderList{}");
        List<OrderDto> orderList = orderDao.findAll();
        return CompletableFuture.completedFuture(orderList);
    }

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<List<StockDto>> stockList() {
        log.info("stockList{}");
        List<StockDto> stockList = stockDao.findAll();
        return CompletableFuture.completedFuture(stockList);
    }

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<List<LogDto>> logList() {
        log.info("logList{}");
        List<LogDto> stockList = logDao.findAll();
        return CompletableFuture.completedFuture(stockList);
    }
}