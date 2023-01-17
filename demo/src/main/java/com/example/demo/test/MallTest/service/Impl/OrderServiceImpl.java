package com.example.demo.test.MallTest.service.Impl;

import com.example.demo.test.MallTest.dao.OrderDao;
import com.example.demo.test.MallTest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
}