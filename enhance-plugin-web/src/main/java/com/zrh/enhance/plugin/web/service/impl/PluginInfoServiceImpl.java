package com.zrh.enhance.plugin.web.service.impl;

import com.zrh.enhance.plugin.web.dao.PluginInfoMapper;
import com.zrh.enhance.plugin.web.model.PluginInfo;
import com.zrh.enhance.plugin.web.service.PluginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PluginInfoServiceImpl
 *
 * @author zhangrh
 * @date 2019/11/9
 **/
@Service
public class PluginInfoServiceImpl implements PluginInfoService {

    @Autowired
    private PluginInfoMapper pluginInfoMapper;


    @Override
    public List<PluginInfo> selectList() {
        return pluginInfoMapper.selectList();
    }
}
