package com.example.demo.test.AsyncTest.service;

import com.example.demo.test.MallTest.pojo.LogDto;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.MallTest.pojo.StockDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface AsyncService {

    /**
     * 异步调用，无返回值
     */
    void asyncTask(String str);

    /**
     * 异步调用，有返回值，Feature处理简单异步任务
     */
    Future<String> asyncTaskForFuture();

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    CompletableFuture<List<OrderDto>> orderList();

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    CompletableFuture<List<StockDto>> stockList();

    /***
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    CompletableFuture<List<LogDto>> logList();
}