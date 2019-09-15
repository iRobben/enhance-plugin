package com.zrh.enhancer.plugin.core;

import java.util.List;

/**
 * @author zhangrenhua
 * @title  插件管理工厂
 * @desc
 * @date 2019/9/15
 */


public interface PluginFactory {
    
    
    /**
     * 安装插件
     * @param pluginDefinition
     */
    void installPlugin(PluginDefinition pluginDefinition);
    
    /**
     * 卸载插件
     * @param pluginDefinition
     */
    void unInstallPlugin(PluginDefinition pluginDefinition);
    
    /**
     * 启用插件
     * @param pluginId
     */
    void activePlugin(String pluginId);
    
    /**
     * 禁用插件
     * @param pluginId
     */
    void disablePlugin(String pluginId);
    
    /**
     * 获取已安装插件
     * @return
     */
    List<PluginDefinition> pluginList();
    
}
