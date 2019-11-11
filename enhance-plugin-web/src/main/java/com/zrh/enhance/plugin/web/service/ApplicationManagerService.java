package com.zrh.enhance.plugin.web.service;

import com.zrh.enhance.plugin.web.dto.ApplicationManagerDTO;
import com.zrh.enhance.plugin.web.model.ApplicationManager;

import java.util.List;

/**
 * ApplicationManagerService
 *
 * @author zhangrh
 * @date 2019/11/9
 **/
public interface ApplicationManagerService {

    int registerApplication(ApplicationManagerDTO applicationManagerDTO);

    List<ApplicationManager> selectList();

}
