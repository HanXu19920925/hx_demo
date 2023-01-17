package com.example.demo.test.ThreadTest.controller;

import com.example.demo.test.ThreadTest.pojo.QueryParamDto;
import com.example.demo.test.ThreadTest.service.ThreadHandleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * springBoot 多线程操作批量数据
 *
 * “进程”是操作系统的概念，一个独立运行的程序，就是一个“进程”。
 * “线程”是由“进程创建”的，一个进程可以创建任意多的线程，每个线程都包含一些代码。线程中的代码会同主进程或者其他线程“同时运行”。
 *
 * “多进程”是同一时间段，同时运行多个程序。
 * "多线程"是一个程序同时启动多个线程，也就是多个代码块同时运行。从而提高程序的运行效率。
 *
 * “并发”是多个线程同时访问同一资源
 * “并行”是多个线程同时开始运行
 *
 * @author hanxu
 */
@RestController
@RequestMapping("/threadHandle")
@Api(tags = "springBoot 多线程操作批量数据")
public class ThreadHandleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ThreadHandleService threadHandleService;

    /**
     * 多线程分段处理批量数据
     */
    @GetMapping("/writeList")
    public void writeList() {
        threadHandleService.writeList();
    }

    /***
     * 多线程分段查询批量数据
     * @param queryParamDto
     * @return
     */
    @GetMapping("/readList")
    public List readList(QueryParamDto queryParamDto) {
        return threadHandleService.readList(queryParamDto);
    }
}