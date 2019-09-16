package com.zrh.enhance.plugin.web.controller;

import com.zrh.enhancer.plugin.core.PluginDefinition;
import com.zrh.enhancer.plugin.core.PluginFactory;
import com.zrh.enhancer.plugin.core.PluginSiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangrenhua
 * @title
 * @desc
 * @date 2019/9/15
 */

@Controller
public class PluginController {
    
    
    @Autowired
    private PluginFactory defaultPluginFactory;


    /**
     * 插件仓库
     * @param model
     * @return
     */
    @GetMapping(value = "/pluginRepository")
    public String pluginRepository(Model model){
        List<PluginSiteInfo> list = new ArrayList<>();
        list.add(new PluginSiteInfo("输出Bean方法执行日志","file:C:/Users/DELL/.m2/repository/com/zrh/enhance-plugin-repository/0.0.1-SNAPSHOT/enhance-plugin-repository-0.0.1-SNAPSHOT.jar"));
        model.addAttribute("list",list);
        return "pluginRepository";
    }


    /**
     * 安装插件
     * @param url
     * @return
     */
    @GetMapping(value = "/installPlugin")
    @ResponseBody
    public String installPlugin(@RequestParam String url){
        PluginDefinition pluginDefinition = new PluginDefinition();
        pluginDefinition.setId(UUID.randomUUID().toString());
        pluginDefinition.setClassName("com.zrh.enhance.plugin.repository.ServerLogPlugin");
        pluginDefinition.setJarRemoteUrl(url);
        pluginDefinition.setVersion("0.0.1-SNAPSHOT");
        pluginDefinition.setName("输出Bean方法执行日志");
        try {
            defaultPluginFactory.installPlugin(pluginDefinition);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * 已安装插件列表
     * @param model
     * @return
     */
    @GetMapping(value = "/pluginList")
    public String pluginList(Model model){
        List<PluginDefinition> pluginDefinitions = defaultPluginFactory.pluginList();
        model.addAttribute("list",pluginDefinitions);
        return "pluginList";
    }


    /**
     * 启用插件
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/activePlugin")
    @ResponseBody
    public String activePlugin(@RequestParam String pluginId){
        try {
            defaultPluginFactory.activePlugin(pluginId);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * 禁用插件
     * @param pluginId
     * @return
     */
    @GetMapping(value = "/disablePlugin")
    @ResponseBody
    public String disablePlugin(@RequestParam String pluginId){
        try {
            defaultPluginFactory.disablePlugin(pluginId);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
