package com.example.demo.test.MallTest.dao;

import com.example.demo.test.MallTest.pojo.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrderDao extends JpaRepository<OrderDto, Integer>, JpaSpecificationExecutor<OrderDto> {

    /***
     * 查询总数
     * @param date
     * @return
     */
    @Query(value = " SELECT COUNT(1) FROM mall_order WHERE to_char(order_date,'yyyy-MM-dd')=:date ", nativeQuery = true)
    int getCount(@Param("date") String date);

    /***
     * 查询记录
     * @param date
     * @return
     */
    @Query(value = " SELECT * FROM mall_order WHERE to_char(order_date,'yyyy-MM-dd')=:date ", nativeQuery = true)
    List<OrderDto> getList(@Param("date") String date);

    /***
     * 单个线程分段查询
     * @param date
     * @param start
     * @param end
     * @return
     */
    @Query(value = " SELECT * FROM (SELECT row_.*, ROWNUM rownum_ FROM (SELECT * FROM mall_order WHERE to_char(order_date,'yyyy-MM-dd')=:date)row_) WHERE rownum_ > :start AND rownum_ <= :end ", nativeQuery = true)
    List<OrderDto> getQueryData(@Param("date") String date, @Param("start") Integer start, @Param("end") Integer end);
}