package com.example.demo.test.UploadTest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public interface UploadService {

    /***
     * 导入
     * @param file
     * @return
     */
    Map importFile(MultipartFile file) throws Exception;
}