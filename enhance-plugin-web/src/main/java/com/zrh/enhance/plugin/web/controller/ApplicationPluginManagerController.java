package com.zrh.enhance.plugin.web.controller;

import com.zrh.enhance.plugin.web.dao.ApplicationPluginManagerMapper;
import com.zrh.enhance.plugin.web.dto.ApplicationPluginDTO;
import com.zrh.enhance.plugin.web.service.ApplicationPluginManagerService;
import com.zrh.enhancer.plugin.dto.CommonResult;
import com.zrh.enhancer.plugin.dto.ResultCode;
import com.zrh.enhancer.plugin.exception.PluginOperateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 应用插件管理
 * @author zhangrenhua
 * @date 2019/9/15
 */
@Controller
public class ApplicationPluginManagerController {

    private static Logger logger = LoggerFactory.getLogger(ApplicationPluginManagerMapper.class);


    @Autowired
    private ApplicationPluginManagerService applicationPluginManagerService;

    /**
     * 应用插件管理列表
     * @param model
     * @return
     */
    @GetMapping(value = "/applicationPluginList")
    public String applicationPluginList(Model model, @RequestParam Long applicationId){
        List<ApplicationPluginDTO> applicationPluginDTOList = applicationPluginManagerService.queryApplicationPluginList(applicationId);
        model.addAttribute("list",applicationPluginDTOList);
        return "applicationPluginList";
    }

    /**
     * 安装插件
     * @param applicationId
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/installPlugin")
    @ResponseBody
    public CommonResult installPlugin(@RequestParam Long applicationId, @RequestParam Long pluginId){
        try {
            applicationPluginManagerService.installPlugin(applicationId,pluginId);
        }catch (PluginOperateException e){
            logger.error(e.getErrorMsg(),e);
            return CommonResult.failed(e.getErrorCode(),e.getErrorMsg());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return CommonResult.failed(ResultCode.EXCEPTION);
        }
        return CommonResult.success(null);
    }

    /**
     * 卸载插件
     * @param applicationId
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/unInstallPlugin")
    @ResponseBody
    public CommonResult unInstallPlugin(@RequestParam Long applicationId, @RequestParam Long pluginId){
        try {
            applicationPluginManagerService.unInstallPlugin(applicationId,pluginId);
        }catch (PluginOperateException e){
            logger.error(e.getErrorMsg(),e);
            return CommonResult.failed(e.getErrorCode(),e.getErrorMsg());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return CommonResult.failed(ResultCode.EXCEPTION);
        }
        return CommonResult.success(null);
    }


    /**
     * 启用插件
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/activePlugin")
    @ResponseBody
    public CommonResult activePlugin(@RequestParam Long applicationId, @RequestParam Long pluginId){
        try {
            applicationPluginManagerService.activePlugin(applicationId,pluginId);
        }catch (PluginOperateException e){
            logger.error(e.getErrorMsg(),e);
            return CommonResult.failed(e.getErrorCode(),e.getErrorMsg());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return CommonResult.failed(ResultCode.EXCEPTION);
        }
        return CommonResult.success(StringUtils.EMPTY);
    }


    /**
     * 禁用插件
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/disablePlugin")
    @ResponseBody
    public CommonResult disablePlugin(@RequestParam Long applicationId, @RequestParam Long pluginId){
        try {
            applicationPluginManagerService.disablePlugin(applicationId,pluginId);
        }catch (PluginOperateException e){
            logger.error(e.getErrorMsg(),e);
            return CommonResult.failed(e.getErrorCode(),e.getErrorMsg());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return CommonResult.failed(ResultCode.EXCEPTION);
        }
        return CommonResult.success(StringUtils.EMPTY);
    }
}
