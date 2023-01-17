package com.example.demo.test.AsyncTest.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.test.AsyncTest.service.AsyncService;
import com.example.demo.test.MallTest.pojo.LogDto;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.MallTest.pojo.StockDto;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.*;

/**
 * springBoot Async异步调用
 * @author hanxu
 */
@RestController
@RequestMapping("/async")
@Api(tags = "springBoot Async异步调用")
public class AsyncController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncService asyncService;

    /**
     * 异步调用，无返回值
     */
    @GetMapping("/asyncTask")
    public void asyncTask() {
        for (int i = 1; i <= 10; i++) {
            asyncService.asyncTask(i+" ");
        }
    }

    /**
     * 异步调用，有返回值，Feature处理简单异步任务
     */
    @GetMapping("/asyncTaskForFuture")
    public String asyncTaskForFuture() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> asyncTaskForFuture = asyncService.asyncTaskForFuture();
        //在设置时间内未返回结果，会直接排除异常TimeoutException，messages为null
        String result = asyncTaskForFuture.get(60, TimeUnit.SECONDS);
        if(StringUtils.isNotEmpty(result)){
            return asyncTaskForFuture.get();
        }else{
            return null;
        }
    }

    /**
     * 异步调用，有返回值，CompletableFuture可以将多个异步任务进行复杂的组合
     */
    @GetMapping("/asyncTaskForFuture_")
    public String asyncTaskForFuture_() throws InterruptedException, ExecutionException {
        CompletableFuture<List<OrderDto>> orderList = asyncService.orderList();
        CompletableFuture<List<StockDto>> stockList = asyncService.stockList();
        CompletableFuture<List<LogDto>> logList = asyncService.logList();
        // 等待所有任务都执行完
        CompletableFuture.allOf(orderList, stockList, logList).join();
        while(true) {
            //判断异步任务是否完成
            if(orderList.isDone() && stockList.isDone() && logList.isDone()) {
                break;
            }
            Thread.sleep(100);
        }
        // 获取每个任务的返回结果
        System.out.println("查询订单信息" + JSON.toJSONString(orderList.get()));
        System.out.println("查询库存信息" + JSON.toJSONString(stockList.get()));
        System.out.println("查询日志信息" + JSON.toJSONString(logList.get()));
        return "success";
    }
}