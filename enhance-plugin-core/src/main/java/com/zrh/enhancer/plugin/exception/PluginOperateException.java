package com.zrh.enhancer.plugin.exception;

/**
 * @author zhangrenhua
 * @title  插件操作异常
 * @desc
 * @date 2019/9/15
 */


public class PluginOperateException extends RuntimeException {
    private static final long serialVersionUID = 8389519508440007578L;
    
    
    private Long errorCode;
    
    private String errorMsg;
    
    public PluginOperateException(Long errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public PluginOperateException(String message, Long errorCode, String errorMsg) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public PluginOperateException(String message, Throwable cause, Long errorCode, String errorMsg) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public PluginOperateException(Throwable cause, Long errorCode, String errorMsg) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public PluginOperateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Long errorCode, String errorMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public Long getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
