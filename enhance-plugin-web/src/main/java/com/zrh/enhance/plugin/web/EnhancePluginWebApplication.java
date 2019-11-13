package com.zrh.enhance.plugin.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangrenhua
 */
@SpringBootApplication
@MapperScan("com.zrh.enhance.plugin.web.dao")
public class EnhancePluginWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnhancePluginWebApplication.class, args);
	}

}
