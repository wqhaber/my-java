package com.it18zhang.umeng.controller;

import com.it18zhang.umeng.service.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/26.
 */
@Controller
@RequestMapping("/stat")
public class StatController {
    
    @Resource
    private StatService ss;
    
    @RequestMapping("/newUser")
    public String newUsers() {
        System.out.println("hello world");
        System.out.println(ss.toString());
        return null;
    }
}
