package com.zrh.enhance.plugin.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = "classpath:pluginConfig.xml")
public class ClientRegistryConfig {

}
