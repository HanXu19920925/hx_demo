package com.example.demo.test.MallTest.service.Impl;

import com.example.demo.test.MallTest.dao.LogDao;
import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.dao.StockDao;
import com.example.demo.test.MallTest.pojo.LogDto;
import com.example.demo.test.MallTest.pojo.OrderDto;
import com.example.demo.test.MallTest.pojo.StockDto;
import com.example.demo.test.MallTest.service.LogService;
import com.example.demo.test.MallTest.service.MallService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MallServiceImpl implements MallService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private LogService logService;

    /***
     * 第一种事务控制
      * @param stockId
     * @return
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String saveAndUpdate(String stockId) {
//        StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
//        if(stockDto.getStockNum() == 0){
//            return "当前无操作记录";
//        }
//        String orderId = String.valueOf(UUID.randomUUID());
//        String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        OrderDto orderDto = new OrderDto();
//        orderDto.setOrderId(orderId);
//        orderDto.setOrderNo(orderNo);
//        orderDto.setOrderDate(new Date());
//        orderDao.save(orderDto);
//        System.out.println(1/0);
//        stockDto.setStockNum(stockDto.getStockNum()-1);
//        stockDao.save(stockDto);
//        return "操作成功";
//    }

    /***
     * 第二种事务控制
     * @param stockId
     * @return
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String saveAndUpdate(String stockId) {
//        try {
//            StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
//            if(stockDto.getStockNum() == 0){
//                return "当前无操作记录";
//            }
//            String orderId = String.valueOf(UUID.randomUUID());
//            String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//            OrderDto orderDto = new OrderDto();
//            orderDto.setOrderId(orderId);
//            orderDto.setOrderNo(orderNo);
//            orderDto.setOrderDate(new Date());
//            orderDao.save(orderDto);
//            System.out.println(1/0);
//            stockDto.setStockNum(stockDto.getStockNum()-1);
//            stockDao.save(stockDto);
//            return "操作成功";
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new RuntimeException();
//        }
//    }

    /***
     * 第三种事务控制
     * @param stockId
     * @return
     */
//    @Override
//    @Transactional
//    public String saveAndUpdate(String stockId) {
//        String result = "操作失败";
//        try {
//            MallServiceImpl proxyObj = (MallServiceImpl) AopContext.currentProxy();
//            result = proxyObj.opertionStockOrder(stockId);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//        LogDto logDto = new LogDto();
//        logDto.setLogId(8);
//        logDto.setLogInfo("一直执行");
//        logDao.save(logDto);
//        return result;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public String opertionStockOrder(String stockId) {
//        StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
//        if(stockDto.getStockNum() == 0){
//            return "当前无操作记录";
//        }
//        String orderId = String.valueOf(UUID.randomUUID());
//        String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        OrderDto orderDto = new OrderDto();
//        orderDto.setOrderId(orderId);
//        orderDto.setOrderNo(orderNo);
//        orderDto.setOrderDate(new Date());
//        orderDao.save(orderDto);
//        stockDto.setStockNum(stockDto.getStockNum()-1);
//        stockDao.save(stockDto);
//        System.out.println(1/0);
//        return "操作成功";
//    }

    /***
     * 第四种事务控制
     * @param stockId
     * @return
     */
//    @Override
//    @Transactional
//    public String saveAndUpdate(String stockId) {
//        String result = "操作失败";
//        MallServiceImpl proxyObj = (MallServiceImpl) AopContext.currentProxy();
//        for(int i = 0; i<=1; i++){
//            try {
//                StockDto stockDto = stockDao.getById(i);
//                result = proxyObj.opertionStockOrder(stockDto);
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//                continue;
//            }
//        }
//        return result;
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public String opertionStockOrder(StockDto stockDto) {
//        String orderId = String.valueOf(UUID.randomUUID());
//        String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        OrderDto orderDto = new OrderDto();
//        orderDto.setOrderId(orderId);
//        orderDto.setOrderNo(orderNo);
//        orderDto.setOrderDate(new Date());
//        orderDao.save(orderDto);
//        //System.out.println(1/0);
//        stockDto.setStockNum(stockDto.getStockNum()-1);
//        stockDao.save(stockDto);
//        return "操作成功";
//    }

    /***
     * 第五种事务控制
     * @param stockId
     * @return
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String saveAndUpdate(String stockId) {
//        try {
//            StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
//            if(stockDto.getStockNum() == 0){
//                return "当前无操作记录";
//            }
//            String orderId = String.valueOf(UUID.randomUUID());
//            String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//            OrderDto orderDto = new OrderDto();
//            orderDto.setOrderId(orderId);
//            orderDto.setOrderNo(orderNo);
//            orderDto.setOrderDate(new Date());
//            orderDao.save(orderDto);
//            stockDto.setStockNum(stockDto.getStockNum()-1);
//            stockDao.save(stockDto);
//            System.out.println(1/0);
//            logService.saveLog();
//            return "操作成功";
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new RuntimeException();
//        }
//    }

    /***
     * 第六种事务控制
     * @param stockId
     * @return
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String saveAndUpdate(String stockId) {
//        try {
//            StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
//            if(stockDto.getStockNum() == 0){
//                return "当前无操作记录";
//            }
//            String orderId = String.valueOf(UUID.randomUUID());
//            String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//            OrderDto orderDto = new OrderDto();
//            orderDto.setOrderId(orderId);
//            orderDto.setOrderNo(orderNo);
//            orderDto.setOrderDate(new Date());
//            orderDao.save(orderDto);
//            stockDto.setStockNum(stockDto.getStockNum()-1);
//            stockDao.save(stockDto);
//            logService.saveLog();
//            //System.out.println(1/0);
//            return "操作成功";
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new RuntimeException();
//        }
//    }

    /***
     * 第七种事务控制
     * @param stockId
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String saveAndUpdate(String stockId) {
        StockDto stockDto = stockDao.getById(Integer.valueOf(stockId));
        if(stockDto.getStockNum() == 0){
            return "当前无操作记录";
        }
        String orderId = String.valueOf(UUID.randomUUID());
        String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderId);
        orderDto.setOrderNo(orderNo);
        orderDto.setOrderDate(new Date());
        orderDao.save(orderDto);
        stockDto.setStockNum(stockDto.getStockNum()-1);
        stockDao.save(stockDto);
        logService.saveLog();
        //System.out.println(1/0);
        return "操作成功";
    }
}