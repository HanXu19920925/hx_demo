package com.example.demo.test.MallTest.service.Impl;

import com.example.demo.test.MallTest.dao.StockDao;
import com.example.demo.test.MallTest.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;
}