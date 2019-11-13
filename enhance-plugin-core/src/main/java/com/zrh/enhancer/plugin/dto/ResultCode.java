package com.zrh.enhancer.plugin.dto;

/**
 * 错误码
 * @author zhangrh
 */
public enum ResultCode {
    /**
     *
     */
    SUCCESS(0, "操作成功"),
    PARAM_EMPTY(1000, "参数缺失"),
    APPLICATION_NOT_EXIST(1001, "应用不存在"),
    PLUGIN_NOT_EXIST(1002, "插件不存在"),
    INSTALL_FAILED(1003, "安装插件失败"),
    UNINSTALL_FAILED(1004, "卸载插件失败"),
    ACTIVE_FAILED(1005, "激活插件失败"),
    DISABLE_FAILED(1005, "禁用插件失败"),
    EXCEPTION(2000,"未知异常");




    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
