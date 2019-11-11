package com.zrh.enhance.plugin.web.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * ApplicationPluginDTO
 *
 * @author zhangrh
 * @date 2019/11/11
 **/
public class ApplicationPluginDTO implements Serializable {

    private String pluginName;

    private String version;

    private String className;

    private Integer installStatus;

    private Integer activeStatus;

    private Date updateTime;
}
