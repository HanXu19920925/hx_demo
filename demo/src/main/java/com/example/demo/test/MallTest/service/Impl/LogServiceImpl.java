package com.example.demo.test.MallTest.service.Impl;

import com.example.demo.test.MallTest.dao.LogDao;
import com.example.demo.test.MallTest.pojo.LogDto;
import com.example.demo.test.MallTest.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

//    public void saveLog(){
//        LogDto logDto = new LogDto();
//        logDto.setLogId(1);
//        logDto.setLogInfo("一直执行");
//        logDao.save(logDto);
//    }

    public void saveLog(){
        //System.out.println(1/0);
        LogDto logDto = new LogDto();
        logDto.setLogId(1);
        logDto.setLogInfo("一直执行");
        logDao.save(logDto);
    }
}