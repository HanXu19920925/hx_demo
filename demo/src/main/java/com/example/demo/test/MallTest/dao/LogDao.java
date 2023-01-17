package com.example.demo.test.MallTest.dao;

import com.example.demo.test.MallTest.pojo.LogDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDao extends JpaRepository<LogDto, Integer>, JpaSpecificationExecutor<LogDto> {

}