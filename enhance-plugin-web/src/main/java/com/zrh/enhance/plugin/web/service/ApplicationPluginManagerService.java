package com.zrh.enhance.plugin.web.service;

import com.zrh.enhance.plugin.web.dto.ApplicationPluginDTO;

import java.util.List;

/**
 * ApplicationPluginManagerService
 *
 * @author zhangrh
 * @date 2019/11/9
 **/
public interface ApplicationPluginManagerService {

    void installPlugin(Long applicationId,Long pluginId);

    void unInstallPlugin(Long applicationId,Long pluginId);

    void activePlugin(Long applicationId,Long pluginId);

    void disablePlugin(Long applicationId,Long pluginId);

    List<ApplicationPluginDTO> queryApplicationPluginList(Long applicationId);

}
