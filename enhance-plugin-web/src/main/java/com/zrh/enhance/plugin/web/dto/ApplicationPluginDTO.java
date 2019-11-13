package com.zrh.enhance.plugin.web.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ApplicationPluginDTO
 *
 * @author zhangrh
 * @date 2019/11/11
 **/
@Data
public class ApplicationPluginDTO implements Serializable {

    private Long pluginId;

    private String pluginName;

    private Long applicationId;

    private String version;

    private String className;

    private Integer installStatus;

    private Integer activeStatus;

    private Date updateTime;


}
