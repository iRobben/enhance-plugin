package com.zrh.enhance.plugin.web.controller;


import com.zrh.enhance.plugin.web.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangrenhua
 * @title
 * @desc
 * @date 2019/9/15
 */

@Controller
public class DemoController {
    
    
    @Autowired
    private DemoService demoService;



    @GetMapping(value = "/sayHello")
    @ResponseBody
    public String sayHello(){
        String result = demoService.sayHello("renhua");
        return result;
    }


}
