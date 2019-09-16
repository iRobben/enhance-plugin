package com.zrh.enhance.plugin.web.service.impl;

import com.zrh.enhance.plugin.web.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/9/16
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {

        return "hello " + name;
    }
}
