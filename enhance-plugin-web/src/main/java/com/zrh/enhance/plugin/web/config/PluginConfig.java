package com.zrh.enhance.plugin.web.config;

import com.zrh.enhancer.plugin.core.DefaultPluginFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangrenhua
 * @title
 * @desc
 * @date 2019/9/15
 */

@Configuration
public class PluginConfig {
    
    @Bean
    public DefaultPluginFactory initPluginFactory(){
        return new DefaultPluginFactory();
    }
}
