package com.zrh.enhance.plugin.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zhangrh
 */
@Data
public class ApplicationManagerDTO implements Serializable {

    @NotBlank
    private String applicationName;
    @NotBlank
    private String applicationIpAddress;

}
