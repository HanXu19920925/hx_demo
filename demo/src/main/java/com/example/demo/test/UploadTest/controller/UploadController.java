package com.example.demo.test.UploadTest.controller;

import com.example.demo.test.UploadTest.service.UploadService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * springBoot 上传
 * @author hanxu
 */
@Controller
@Api(tags = "springBoot upload thymeleaf")
public class UploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UploadService uploadService;

    @GetMapping("/upload")
    public ModelAndView upload(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/upload");
        return modelAndView;
    }

    /***
     * 导入
     * @param file
     * @return
     */
    @PostMapping(value = "/importFile")
    public Map importFile(@RequestPart("file") MultipartFile file) throws Exception {
        return uploadService.importFile(file);
    }
}