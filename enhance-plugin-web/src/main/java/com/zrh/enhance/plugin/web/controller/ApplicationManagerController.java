package com.zrh.enhance.plugin.web.controller;

import com.zrh.enhance.plugin.web.dto.ApplicationManagerDTO;
import com.zrh.enhance.plugin.web.model.ApplicationManager;
import com.zrh.enhance.plugin.web.service.ApplicationManagerService;
import com.zrh.enhancer.plugin.dto.CommonResult;
import com.zrh.enhancer.plugin.dto.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhangrenhua
 * @title
 * @desc
 * @date 2019/9/15
 */

@Controller
public class ApplicationManagerController {


    @Autowired
    private ApplicationManagerService applicationManagerService;


    /**
     * 应用列表
     * @param model
     * @return
     */
    @GetMapping(value = "/applicationList")
    public String applicationList(Model model){
        List<ApplicationManager> applicationManagerList = applicationManagerService.selectList();
        model.addAttribute("list",applicationManagerList);
        return "applicationList";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult register(@RequestBody @Valid ApplicationManagerDTO applicationManagerDTO, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.failed(ResultCode.PARAM_EMPTY);
        }
        applicationManagerService.registerApplication(applicationManagerDTO);
        return CommonResult.success(null);
    }
}
