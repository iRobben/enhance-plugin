package com.zrh.enhance.plugin.web.service.impl;

import com.zrh.enhance.plugin.web.dao.ApplicationManagerMapper;
import com.zrh.enhance.plugin.web.dto.ApplicationManagerDTO;
import com.zrh.enhance.plugin.web.model.ApplicationManager;
import com.zrh.enhance.plugin.web.service.ApplicationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangrh
 */
@Service
public class ApplicationManagerServiceImpl implements ApplicationManagerService {

    @Autowired
    private ApplicationManagerMapper applicationManagerMapper;

    @Override
    public List<ApplicationManager> selectList() {
        return applicationManagerMapper.selectList();
    }

    @Override
    public int registerApplication(ApplicationManagerDTO applicationManagerDTO) {
        String applicationIpAddress = applicationManagerDTO.getApplicationIpAddress();
        ApplicationManager existApplicationManager = applicationManagerMapper.selectByApplicationAddress(applicationIpAddress);
        if(existApplicationManager == null){
            ApplicationManager applicationManager = new ApplicationManager();
            applicationManager.setApplicationName(applicationManagerDTO.getApplicationName());
            applicationManager.setApplicationIpAddress(applicationIpAddress);
            applicationManager.setCreateOperName(applicationManagerDTO.getApplicationName());
            applicationManager.setUpdateOperName(applicationManagerDTO.getApplicationName());
            return applicationManagerMapper.insertSelective(applicationManager);
        }else {
            existApplicationManager.setUpdateTime(new Date());
            return applicationManagerMapper.updateByPrimaryKeySelective(existApplicationManager);
        }
    }

    @Override
    public ApplicationManager selectOne(Long id) {
        return applicationManagerMapper.selectByPrimaryKey(id);
    }
}
