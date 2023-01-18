package com.example.demo.test.UploadTest.service.Impl;

import com.example.demo.test.UploadTest.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UploadServiceImpl implements UploadService {

    /***
     * 导入
     * @param file
     * @return
     */
    @Override
    public Map importFile(MultipartFile file) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        System.out.println("导入");
        return returnMap;
    }
}