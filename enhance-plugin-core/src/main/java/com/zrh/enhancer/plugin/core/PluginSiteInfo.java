package com.zrh.enhancer.plugin.core;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 插件站点信息
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/9/16
 */
@Data
public class PluginSiteInfo implements Serializable{
    private static final long serialVersionUID = 3598650826479169826L;

    private String pluginName;

    private String url;

    public PluginSiteInfo(String pluginName, String url) {
        this.pluginName = pluginName;
        this.url = url;
    }
}
