package com.example.demo.test.MallTest.dao;

import com.example.demo.test.MallTest.pojo.StockDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<StockDto, Integer>, JpaSpecificationExecutor<StockDto> {

    @Query(value = " FROM StockDto WHERE stockId =:stockId ")
    StockDto getById(@Param("stockId") Integer stockId);
}