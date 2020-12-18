package com.wzs.multidatasource.controller;

import com.wzs.multidatasource.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试类
 * @Auther: wuzs
 * @Date: 2020/12/18 10:00
 * @Version: 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    public String test1() {
        return "" + testService.test1();
    }

    @RequestMapping("/test2")
    public String test2() {
        return "" + testService.test2();
    }

    @RequestMapping("/test3")
    public String test3() {
        return "" + testService.testTra();
    }
}
