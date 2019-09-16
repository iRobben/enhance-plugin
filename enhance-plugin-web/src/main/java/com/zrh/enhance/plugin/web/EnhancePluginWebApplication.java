package com.zrh.enhance.plugin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

/**
 * @author zhangrenhua
 */
@SpringBootApplication
public class EnhancePluginWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnhancePluginWebApplication.class, args);
	}

}
