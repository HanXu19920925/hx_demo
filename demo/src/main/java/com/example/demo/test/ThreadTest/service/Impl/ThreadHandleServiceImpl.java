package com.example.demo.test.ThreadTest.service.Impl;

import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.ThreadTest.dao.OrderHandleList;
import com.example.demo.test.ThreadTest.dao.OrderThreadList;
import com.example.demo.test.ThreadTest.dao.OrderThreadTransaction;
import com.example.demo.test.ThreadTest.pojo.QueryParamDto;
import com.example.demo.test.ThreadTest.service.ThreadHandleService;
import com.example.demo.test.ThreadTest.service.ThreadHandleTransactionService;
import com.example.demo.util.SplitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/***
 * Spring相关数据库连接信息都放在了threadLocal中，所以不同的线程享用不同的连接信息，所以多线程操作不存在于一个事务中
 * 线程池在什么情况下才会去创建新得线程呢？
 * 当线程数量与核心数量一致的时候。并不是当任务来了就直接创建一个新的线程去执行，
 * 而是先放到缓冲队列中，队列满的时候才会去判断最大线程数 从而决定是执行拒绝策略还是创建新的线程
 */
@Slf4j
@Service
public class ThreadHandleServiceImpl implements ThreadHandleService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ThreadHandleTransactionService threadHandleTransactionService;

    //根据本机CPU的核数配置合适数量的线程处理数
    private static final int corePoolSize = 5;

    /**
     * 多线程分段处理批量数据(事务控制)--方式一批处理
     */
    @Override
    public void writeList() {
        //接收数据
        List<OrderDto> getList = orderDao.getList("2023-01-06");

        //定义线程数量
        int threadNum;
        if (getList.size() < corePoolSize){
            threadNum = getList.size();
        }else {
            threadNum = corePoolSize;
        }

        //每个线程处理的数据量
        int num = (getList.size() / threadNum) + 1;

        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        //子线程提交数量
        CountDownLatch threadLatch = new CountDownLatch(threadNum);

        //判断主线程是否提交
        CountDownLatch mainLatch = new CountDownLatch(1);

        //判断子线程任务是否有错误
        AtomicBoolean isError = new AtomicBoolean(false);

        for (int i = 0; i < threadNum; i++) {
            //单个线程处理的数据集合
            List<OrderDto> threadData = getList.stream().skip(i * num).limit(num).collect(Collectors.toList());
            //线程池提交
            executorService.execute(() -> {
                threadHandleTransactionService.ThreadHandleTransaction(threadData, threadLatch, mainLatch, isError);
            });
        }

        try {
            //倒计时锁设置超时时间30s
            boolean await = threadLatch.await(30, TimeUnit.SECONDS);
            if (!await) {
                //等待超时,事务回滚
                isError.set(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            isError.set(true);
        }

        //切换到子线程执行
        mainLatch.countDown();

        //关闭线程池
        executorService.shutdown();
        log.info("主线程完成{}");
    }

//    /***
//     * 多线程分段处理批量数据(事务控制)--方式二批处理
//     */
//    @Override
//    public void writeList() {
//        //接收数据
//        List<OrderDto> getList = orderDao.getList("2023-01-06");
//
//        //定义线程数量
//        int threadNum;
//        if (getList.size() < corePoolSize){
//            threadNum = getList.size();
//        }else {
//            threadNum = corePoolSize;
//        }
//
//        //创建线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//
//        //平衡分组
//        List<OrderDto>[] orderDtoList = SplitUtil.spiltDataList(getList, threadNum);
//
//        //线程池提交
//        for (List<OrderDto> orderDto : orderDtoList) {
//            OrderThreadTransaction orderThreadTransaction = new OrderThreadTransaction(orderDao, orderDto);
//            executorService.execute(orderThreadTransaction);
//        }
//    }
//
//    /***
//     * 多线程分段查询批量数据--方式一批查询
//     * @param queryParamDto
//     * @return
//     */
//    @Override
//    public List readList(QueryParamDto queryParamDto) {
//        //开始时间
//        long startTime = System.currentTimeMillis();
//
//        //查询总数
//        int count = orderDao.getCount(queryParamDto.getDate());
//
//        //定义线程数量
//        int threadNum;
//        if (count < corePoolSize){
//            threadNum = count;
//        }else {
//            threadNum = corePoolSize;
//        }
//
//        //每个线程查询的数据量
//        int num = (count / threadNum) + 1;
//
//        //封装Callable获取结果
//        List<Callable<List>> tasks = new ArrayList<>();
//        for (int i = 0; i < threadNum; i++) {
//            int startNum = i*num;
//            int endNum  = startNum+num;
//            Callable<List> res = new OrderHandleList(queryParamDto, startNum, endNum);
//            tasks.add(res);
//        }
//
//        //封装合并结果
//        List<List> result = new ArrayList<>();
//
//        //创建线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        try {
//            //Future获取结果
//            List<Future<List>> futures = executorService.invokeAll(tasks);
//            //处理单个线程返回结果
//            if(futures != null && futures.size() > 0){
//                //迭代结果
//                for (Future<List> future : futures){
//                    //合并结果
//                    result.addAll(future.get());
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } finally {
//            //关闭线程池
//            executorService.shutdown();
//            while (true){
//                if (executorService.isTerminated()){
//                    log.info("多线程分段查询批量数据任务已完成！");
//                    break;
//                }
//            }
//        }
//        long endTime = System.currentTimeMillis();
//        log.info("多线程分段查询批量数据共计耗时{}"+(endTime-startTime)+"ms");
//        log.info("多线程分段查询批量数据总数量{}"+result.size());
//        return result;
//    }

    /***
     * 单个线程分段查询
     * @param queryParamDto
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<OrderDto> getQueryData(QueryParamDto queryParamDto, Integer start, Integer end) {
        log.info("索引起始{}"+start+"索引结束{}"+end);
        List<OrderDto> getQueryData = orderDao.getQueryData(queryParamDto.getDate(), start, end);
        return getQueryData;
    }

    /***
     * 多线程分段查询批量数据--方式二批查询
     * @param queryParamDto
     * @return
     */
    @Override
    public List readList(QueryParamDto queryParamDto) {
        //开始时间
        long startTime = System.currentTimeMillis();

        //查询总数
        int count = orderDao.getCount(queryParamDto.getDate());

        //定义线程数量
        int threadNum;
        if (count < corePoolSize){
            threadNum = count;
        }else {
            threadNum = corePoolSize;
        }

        //每个线程查询的数据量
        int num = (count / threadNum) + 1;

        //封装Callable获取结果
        List<Future<List>> futureList = new ArrayList();

        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        //计数器
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        //线程池提交
        for (int i = 0; i < threadNum; i++) {
            int startNum = i*num;
            int endNum  = startNum+num;
            OrderThreadList task = new OrderThreadList(orderDao, queryParamDto, startNum, endNum, countDownLatch);
            futureList.add(executorService.submit(task));
        }

        //封装合并结果
        List<List> result = new ArrayList<>();

        try {
            //主线程等待所有子线程完成任务后再执行
            countDownLatch.await();
            if(futureList != null && futureList.size() > 0){
                //迭代结果
                for (Future<List> future : futureList){
                    //合并结果
                    result.addAll(future.get());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            executorService.shutdown();
            while (true){
                if (executorService.isTerminated()){
                    log.info("多线程分段查询批量数据任务已完成！");
                    break;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("多线程分段查询批量数据共计耗时{}"+(endTime-startTime)+"ms");
        log.info("多线程分段查询批量数据总数量{}"+result.size());
        return result;
    }
}