package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/***
 * 多线程异步调用，一般还会配置一个线程池，异步的方法交给特定的线程池完成
 * 若需要在关闭线程池时等待当前调度任务完成后才开始关闭，可以通过配置，进行优雅的停机策略
 * 通过setWaitForTasksToCompleteOnShutdown(true)和setAwaitTerminationSeconds方法即可
 * @Async注解会在以下几个场景失效:
 * 1.异步方法使用static关键词修饰
 * 2.SpringBoot应用中没有添加@EnableAsync注解；
 * 3.在同一个类中，一个方法调用另外一个有@Async注解的方法，注解不会生效,原因是@Async注解的方法，是在代理类中执行的
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    //核心线程数(线程池创建初始化的线程数)，必须小于等于最大线程数
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    //最大线程数(线程池最大线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程)
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;

    //队列最大容量(用来缓冲执行任务的队列，根据核心线程数以及系统对任务响应时间判定，通常：(CorePoolSize/TaskTime)*ResponseTime)
    private static final int MAX_QUEUE_CAPACITY = 500;

    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        asyncTaskExecutor.setQueueCapacity(MAX_QUEUE_CAPACITY);
        //允许线程的空闲时间60秒，当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        asyncTaskExecutor.setKeepAliveSeconds(60);
        //线程池名的前缀，设置之后可以方便我们定位处理任务所在的线程池
        asyncTaskExecutor.setThreadNamePrefix("async-task-");
        //拒绝策略
        asyncTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //setWaitForTasksToCompleteOnShutdown:表明等待所有线程执行完，默认为false
        asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //setAwaitTerminationSeconds:等待的时间，因为不能无限的等待下去
        asyncTaskExecutor.setAwaitTerminationSeconds(60);
        //初始化
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}