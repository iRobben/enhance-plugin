package com.zrh.enhancer.plugin.exception;

/**
 * @author zhangrenhua
 * @title
 * @desc
 * @date 2019/9/15
 */


public enum ExceptionMessageEnum {
   
    SUCCESS(0L,"成功")
    
    ;
    
    private Long errorCode;
    
    private String errorMsg;
    
    ExceptionMessageEnum(Long errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public Long getErrorCode() {
        return errorCode;
    }
    
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
}
