package com.zrh.enhancer.plugin.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangrenhua
 * @title  插件定义
 * @desc
 * @date 2019/9/15
 */

@Data
public class PluginDefinition implements Serializable {
    private static final long serialVersionUID = 5419235409460120006L;
    
    private String id;
    
    private String name;
    
    private String className;
    
    /** 插件jar 地址 */
    private String jarRemoteUrl;
    
    /** 插件版本号 */
    private String version;
    
    /** 是否启用 */
    private Boolean isActive;
    
}
