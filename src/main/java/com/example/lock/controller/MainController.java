package com.example.lock.controller;

import com.example.lock.service.impl.EnquiryTaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kongxiangshuai
 */
@RestController
public class MainController {

    final EnquiryTaskService enquiryTaskService;

    public MainController(EnquiryTaskService enquiryTaskService) {
        this.enquiryTaskService = enquiryTaskService;
    }

    @GetMapping("/task")
    public String addTask(String param) {
        enquiryTaskService.createTask(param);
        return "SUCCESS";
    }
}
