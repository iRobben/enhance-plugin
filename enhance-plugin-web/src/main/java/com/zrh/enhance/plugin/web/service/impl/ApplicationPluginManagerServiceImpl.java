package com.zrh.enhance.plugin.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.zrh.enhance.plugin.web.dao.ApplicationManagerMapper;
import com.zrh.enhance.plugin.web.dao.ApplicationPluginManagerMapper;
import com.zrh.enhance.plugin.web.dao.PluginInfoMapper;
import com.zrh.enhance.plugin.web.dto.ApplicationPluginDTO;
import com.zrh.enhance.plugin.web.model.ApplicationManager;
import com.zrh.enhance.plugin.web.model.ApplicationPluginManager;
import com.zrh.enhance.plugin.web.model.PluginInfo;
import com.zrh.enhance.plugin.web.service.ApplicationPluginManagerService;
import com.zrh.enhancer.plugin.dto.CommonResult;
import com.zrh.enhancer.plugin.dto.ResultCode;
import com.zrh.enhancer.plugin.exception.PluginOperateException;
import com.zrh.enhancer.plugin.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApplicationPluginManagerServiceImpl
 *
 * @author zhangrh
 * @date 2019/11/9
 **/
@Service
public class ApplicationPluginManagerServiceImpl implements ApplicationPluginManagerService {


    @Autowired
    private PluginInfoMapper pluginInfoMapper;

    @Autowired
    private ApplicationManagerMapper applicationManagerMapper;

    @Autowired
    private ApplicationPluginManagerMapper applicationPluginManagerMapper;

    private static final String INSTALL_INTERFACE = "/plugin/install";
    private static final String UN_INSTALL_INTERFACE = "/plugin/unInstall";
    private static final String ACTIVE_INTERFACE ="/plugin/active";
    private static final String DISABLE_INTERFACE = "/plugin/disable";

    @Override
    public void installPlugin(Long applicationId, Long pluginId) {
        PluginInfo pluginInfo = pluginInfoMapper.selectByPrimaryKey(pluginId);
        if(pluginInfo == null){
            throw new PluginOperateException(ResultCode.PLUGIN_NOT_EXIST.getCode(),ResultCode.PLUGIN_NOT_EXIST.getMessage());
        }
        ApplicationManager applicationManager = applicationManagerMapper.selectByPrimaryKey(applicationId);
        if(applicationManager == null){
            throw new PluginOperateException(ResultCode.APPLICATION_NOT_EXIST.getCode(),ResultCode.APPLICATION_NOT_EXIST.getMessage());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",pluginId);
        param.put("name",pluginInfo.getName());
        param.put("className",pluginInfo.getClassName());
        param.put("jarRemoteUrl",pluginInfo.getJarRemoteUrl());
        param.put("version",pluginInfo.getVersion());
        param.put("applicationName",applicationManager.getApplicationName());
        String url =  "http://" + applicationManager.getApplicationIpAddress() + INSTALL_INTERFACE;
        String response = HttpClientUtils.httpPostRequest(url,JSON.toJSONString(param));
            if(StringUtils.isNotBlank(response)){
            CommonResult commonResult = JSON.parseObject(response, CommonResult.class);
            if(commonResult.getCode() == ResultCode.SUCCESS.getCode()){
                ApplicationPluginManager applicationPluginManager = new ApplicationPluginManager();
                applicationPluginManager.setApplicationId(applicationId);
                applicationPluginManager.setPluginId(pluginId);
                applicationPluginManager.setInstallStatus(1);
                applicationPluginManager.setActiveStatus(0);
                applicationPluginManager.setCreateOperName(applicationManager.getApplicationName());
                applicationPluginManager.setUpdateOperName(applicationManager.getApplicationName());
                applicationPluginManagerMapper.insertSelective(applicationPluginManager);
            }else {
                throw new PluginOperateException(ResultCode.INSTALL_FAILED.getCode(),ResultCode.INSTALL_FAILED.getMessage() + ":" + commonResult.getMessage());
            }
        }else {
            throw new PluginOperateException(ResultCode.INSTALL_FAILED.getCode(),ResultCode.INSTALL_FAILED.getMessage());
        }
    }

    @Override
    public void unInstallPlugin(Long applicationId, Long pluginId) {
        PluginInfo pluginInfo = pluginInfoMapper.selectByPrimaryKey(pluginId);
        if(pluginInfo == null){
            throw new PluginOperateException(ResultCode.PLUGIN_NOT_EXIST.getCode(),ResultCode.PLUGIN_NOT_EXIST.getMessage());
        }
        ApplicationManager applicationManager = applicationManagerMapper.selectByPrimaryKey(applicationId);
        if(applicationManager == null){
            throw new PluginOperateException(ResultCode.APPLICATION_NOT_EXIST.getCode(),ResultCode.APPLICATION_NOT_EXIST.getMessage());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",pluginId);
        param.put("applicationName",applicationManager.getApplicationName());
        String url = "http://" + applicationManager.getApplicationIpAddress() + UN_INSTALL_INTERFACE;
        String response = HttpClientUtils.httpPostRequest(url,JSON.toJSONString(param));
        if(StringUtils.isNotBlank(response)){
            CommonResult commonResult = JSON.parseObject(response, CommonResult.class);
            if(commonResult.getCode() == ResultCode.SUCCESS.getCode()){
                ApplicationPluginManager applicationPluginManager = applicationPluginManagerMapper.selectByApplicationIdAndPluginId(applicationId, pluginId);
                if(applicationPluginManager != null){
                    ApplicationPluginManager updateApplicationPluginManager = new ApplicationPluginManager();
                    updateApplicationPluginManager.setId(applicationPluginManager.getId());
                    updateApplicationPluginManager.setInstallStatus(0);
                    updateApplicationPluginManager.setUpdateOperName(applicationManager.getApplicationName());
                    updateApplicationPluginManager.setUpdateTime(new Date());
                    applicationPluginManagerMapper.updateByPrimaryKeySelective(updateApplicationPluginManager);
                }
            }else {
                throw new PluginOperateException(ResultCode.UNINSTALL_FAILED.getCode(),ResultCode.UNINSTALL_FAILED.getMessage() + ":" + commonResult.getMessage());
            }
        }else {
            throw new PluginOperateException(ResultCode.UNINSTALL_FAILED.getCode(),ResultCode.UNINSTALL_FAILED.getMessage());
        }
    }

    @Override
    public void activePlugin(Long applicationId, Long pluginId) {
        PluginInfo pluginInfo = pluginInfoMapper.selectByPrimaryKey(pluginId);
        if(pluginInfo == null){
            throw new PluginOperateException(ResultCode.PLUGIN_NOT_EXIST.getCode(),ResultCode.PLUGIN_NOT_EXIST.getMessage());
        }
        ApplicationManager applicationManager = applicationManagerMapper.selectByPrimaryKey(applicationId);
        if(applicationManager == null){
            throw new PluginOperateException(ResultCode.APPLICATION_NOT_EXIST.getCode(),ResultCode.APPLICATION_NOT_EXIST.getMessage());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",pluginId);
        String url = "http://" + applicationManager.getApplicationIpAddress() + ACTIVE_INTERFACE;
        String response = HttpClientUtils.httpPostRequest(url,JSON.toJSONString(param));
        if(StringUtils.isNotBlank(response)){
            CommonResult commonResult = JSON.parseObject(response, CommonResult.class);
            if(commonResult.getCode() == ResultCode.SUCCESS.getCode()){
                ApplicationPluginManager applicationPluginManager = applicationPluginManagerMapper.selectByApplicationIdAndPluginId(applicationId, pluginId);
                if(applicationPluginManager != null){
                    ApplicationPluginManager updateApplicationPluginManager = new ApplicationPluginManager();
                    updateApplicationPluginManager.setId(applicationPluginManager.getId());
                    updateApplicationPluginManager.setActiveStatus(1);
                    updateApplicationPluginManager.setUpdateOperName(applicationManager.getApplicationName());
                    updateApplicationPluginManager.setUpdateTime(new Date());
                    applicationPluginManagerMapper.updateByPrimaryKeySelective(updateApplicationPluginManager);
                }
            }else {
                throw new PluginOperateException(ResultCode.ACTIVE_FAILED.getCode(),ResultCode.ACTIVE_FAILED.getMessage() + ":" + commonResult.getMessage());
            }
        }else {
            throw new PluginOperateException(ResultCode.ACTIVE_FAILED.getCode(),ResultCode.ACTIVE_FAILED.getMessage());
        }
    }

    @Override
    public void disablePlugin(Long applicationId, Long pluginId) {
        PluginInfo pluginInfo = pluginInfoMapper.selectByPrimaryKey(pluginId);
        if(pluginInfo == null){
            throw new PluginOperateException(ResultCode.PLUGIN_NOT_EXIST.getCode(),ResultCode.PLUGIN_NOT_EXIST.getMessage());
        }
        ApplicationManager applicationManager = applicationManagerMapper.selectByPrimaryKey(applicationId);
        if(applicationManager == null){
            throw new PluginOperateException(ResultCode.APPLICATION_NOT_EXIST.getCode(),ResultCode.APPLICATION_NOT_EXIST.getMessage());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",pluginId);
        String url = "http://" + applicationManager.getApplicationIpAddress() + DISABLE_INTERFACE;
        String response = HttpClientUtils.httpPostRequest(url,JSON.toJSONString(param));
        if(StringUtils.isNotBlank(response)){
            CommonResult commonResult = JSON.parseObject(response, CommonResult.class);
            if(commonResult.getCode() == ResultCode.SUCCESS.getCode()){
                ApplicationPluginManager applicationPluginManager = applicationPluginManagerMapper.selectByApplicationIdAndPluginId(applicationId, pluginId);
                if(applicationPluginManager != null){
                    ApplicationPluginManager updateApplicationPluginManager = new ApplicationPluginManager();
                    updateApplicationPluginManager.setId(applicationPluginManager.getId());
                    updateApplicationPluginManager.setActiveStatus(0);
                    updateApplicationPluginManager.setUpdateOperName(applicationManager.getApplicationName());
                    updateApplicationPluginManager.setUpdateTime(new Date());
                    applicationPluginManagerMapper.updateByPrimaryKeySelective(updateApplicationPluginManager);
                }
            }else {
                throw new PluginOperateException(ResultCode.DISABLE_FAILED.getCode(),ResultCode.DISABLE_FAILED.getMessage() + ":" + commonResult.getMessage());
            }
        }else {
            throw new PluginOperateException(ResultCode.DISABLE_FAILED.getCode(),ResultCode.DISABLE_FAILED.getMessage());
        }
    }

    @Override
    public List<ApplicationPluginDTO> queryApplicationPluginList(Long applicationId) {
        return applicationPluginManagerMapper.selectApplicationPluginList(applicationId);
    }
}
