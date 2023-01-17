package com.example.demo.test.ThreadTest.service;

import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.ThreadTest.pojo.QueryParamDto;

import java.util.*;

public interface ThreadHandleService {

    /**
     * 多线程分段处理批量数据
     */
    void writeList();

    /***
     * 多线程分段查询批量数据
     * @param queryParamDto
     * @return
     */
    List readList(QueryParamDto queryParamDto);

    /***
     * 单个线程分段查询
     * @param queryParamDto
     * @param start
     * @param end
     * @return
     */
    List<OrderDto> getQueryData(QueryParamDto queryParamDto, Integer start, Integer end);
}