package com.zrh.enhance.plugin.web.controller;


import com.zrh.enhance.plugin.web.model.PluginInfo;
import com.zrh.enhance.plugin.web.service.PluginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 插件信息
 * @author zhangrh
 */
@Controller
public class PluginInfoController {


    @Autowired
    private PluginInfoService pluginInfoService;

    /**
     * 插件仓库
     * @param model
     * @return
     */
    @GetMapping(value = "/pluginRepository")
    public String pluginRepository(Model model){
        List<PluginInfo> pluginInfos = pluginInfoService.selectList();
        model.addAttribute("list",pluginInfos);
        return "pluginRepository";
    }
}
