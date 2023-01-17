package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/***
 * 线程池工作顺序：核心线程数->任务队列->最大线程数->拒绝策略
 * 创建线程池7个参数：
 * 1.corePoolSize 线程池核心线程数
 * 2.maximumPoolSize能容纳的最大线程数，一般情况下是corePoolSize * 2
 * 3.keepAliveTime空闲线程存活时间
 * 4.unit空闲线程存活的时间单位，MILLISECONDS-毫秒，SECONDS-秒
 * 5.workQueue存放提交但未执行任务的队列
 * 6.threadFactory创建线程的工厂类
 * 7.handler等待队列满后的拒绝策略
 */
public class ThreadPoolExecutorUtil {

    /***
     * createThreadPool1和createThreadPool2均使用execute方法执行任务，通过runnable接口创建线程类
     * 相同点：execute方法提交
     * 不同点：线程类的创建方式
     */
    public static void createThreadPool1(){

        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i<10; i++) {
            final int index = i;
            //匿名内部类方式创建
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //业务处理
                    System.out.println(Thread.currentThread().getName() + " " + index);
                }
            });
        }
        executorService.shutdown();
    }

    public static void createThreadPool2(){
        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i<10; i++) {
            final int index = i;
            //自定义创建线程类
            executorService.execute(new RunnableTask(index));
        }
    }

    static class RunnableTask implements Runnable{
        private int i;

        public RunnableTask(int i){
            this.i = i;
        }

        @Override
        public void run() {
            //业务处理
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

    /***
     * createThreadPool3使用invokeAll方法提交
     * 使用invokeAll批量执行任务，通过Callable接口创建线程类(匿名内部类方式创建线程类)
     * invokeAll作用：等待所有的任务执行完毕后统一返回
     */
    public static void createThreadPool3(){
        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        List<Callable<Object>> tasks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            tasks.add(() -> {
                System.out.println(Thread.currentThread().getName());
                return null;
            });
        }

        try {
            List<Future<Object>> futureList = executorService.invokeAll(tasks);
            //获取全部并发任务的运行结果
            for (Future future : futureList){
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //关闭线程池
        executorService.shutdown();
    }

    /***
     * createThreadPool4使用submit方法提交
     * 使用submit方法执行任务，通过Callable接口创建线程类(匿名内部类方式创建线程类)
     */
    public static void createThreadPool4(){
        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future<Callable>> tasks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            final int index = i;
            Future future = executorService.submit(() -> {
               System.out.println(Thread.currentThread().getName() + " " + index);
               return index;
            });
            tasks.add(future);
        }

        try {
            //获取全部并发任务的运行结果
            for (Future future : tasks){
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //关闭线程池
        executorService.shutdown();
    }

    /***
     * createThreadPool5使用submit方法提交
     * 使用submit方法执行任务，通过Callable接口创建线程类(自定义方式创建线程类)
     */
    public static void createThreadPool5(){
        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future<Callable>> tasks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            final int index = i;
            Future future = executorService.submit(new CallableTask(index));
            tasks.add(future);
        }

        try {
            //获取全部并发任务的运行结果
            for (Future future : tasks){
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //关闭线程池
        executorService.shutdown();
    }

    static class CallableTask implements Callable<Integer>{

        Integer i;

        public CallableTask(Integer i){
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " " + i);
            return i;
        }
    }

    /***
     * 线程池监控
     */
    public static void createThreadPool6() {
        //开始时间
        long startTime = System.currentTimeMillis();
        //核心线程数，必须小于等于最大线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //最大线程数
        int maximumPoolSize = corePoolSize * 2;
        //创建线程池
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for(int i = 0; i < 5; i++){
            executorService.execute(() -> {
                printThreadPoolStatus(executorService);
            });
        }

        executorService.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：线程池监控耗时：" + (endTime - startTime));
    }

    private static void printThreadPoolStatus(ThreadPoolExecutor executor){
        BlockingQueue queue = executor.getQueue();
        System.out.println(
                Thread.currentThread().getName() + "," +
                "当前的线程数量:" + executor.getPoolSize() + "," +
                "核心线程数:" + executor.getCorePoolSize() + "," +
                "最大线程数:" + executor.getMaximumPoolSize() + "," +
                "活动线程数:" + executor.getActiveCount() + "," +
                "任务总数:" + executor.getTaskCount() + "," +
                "任务完成数:" + executor.getCompletedTaskCount() + "," +
                "线程空闲时间:" + executor.getKeepAliveTime(TimeUnit.SECONDS) + "秒," +
                "当前排队线程数:" + queue.size() + "," +
                "队列剩余大小:" + queue.remainingCapacity() + "," +
                "线程池是否关闭:" + executor.isShutdown() + ","
        );
    }

    /***
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        //以下五种方式均通过ThreadPoolExxcutor创建线程池
        //createThreadPool1();
        //createThreadPool2();
        //createThreadPool3();
        //createThreadPool4();
        //createThreadPool5();
        //createThreadPool6();
    }
}