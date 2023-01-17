package com.example.demo.test.ThreadTest.dao;

import com.example.demo.test.ThreadTest.pojo.QueryParamDto;
import com.example.demo.test.ThreadTest.service.ThreadHandleService;
import com.example.demo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
public class OrderHandleList implements Callable<List> {

    //创建上下文对象实例
    public static SpringContextUtil springContextUtil = new SpringContextUtil();

    //查询条件
    private QueryParamDto queryParamDto;

    //索引起始
    private int start;

    //索引结束
    private int end;

    //单个线程分段查询集合
    private List data;

    public OrderHandleList(QueryParamDto queryParamDto, int start, int end) {
        this.queryParamDto = queryParamDto;
        this.start = start;
        this.end = end;
        ThreadHandleService threadHandleService = springContextUtil.getBean(ThreadHandleService.class);
        List getQueryData = threadHandleService.getQueryData(queryParamDto, start, end);
        log.info("当前线程{}"+Thread.currentThread().getName()+"处理数据数量{}"+getQueryData.size());
        data = getQueryData;
    }

    @Override
    public List call() throws Exception {
        return data;
    }
}