package com.zrh.enhance.plugin.client;

import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.HttpServer;
import com.zrh.enhance.plugin.client.http.PluginActiveHandler;
import com.zrh.enhance.plugin.client.http.PluginDisableHandler;
import com.zrh.enhance.plugin.client.http.PluginInstallHandler;
import com.zrh.enhance.plugin.client.http.PluginUnInstallHandler;
import com.zrh.enhancer.plugin.core.PluginFactory;
import com.zrh.enhancer.plugin.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端注册
 * @author zhangrh
 */
public class ClientRegister {

    private static Logger logger = LoggerFactory.getLogger(ClientRegister.class);

    private static final String KEY_CONFIG_FILE = "pluginConfig";

    private static String applicationName;

    private static String applicationHost;

    private static String epRegistry;

    public ClientRegister(PluginFactory pluginFactory,String applicationName,String applicationHost,String epRegistry) {
        //获取配置文件属性
        ClientRegister.applicationName =  applicationName;
        ClientRegister.applicationHost =  applicationHost;
        ClientRegister.epRegistry =  epRegistry;
        try {
            //向管理平台注册应用信息
            InetAddress localhost = InetAddress.getLocalHost();
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("applicationName", applicationName);
            paramMap.put("applicationIpAddress", localhost.getHostAddress() + ":" + applicationHost);
            HttpClientUtils.httpPostRequest(epRegistry, JSON.toJSONString(paramMap));
            //启动http服务
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(applicationHost)), 0);
            server.createContext("/plugin/install", new PluginInstallHandler(pluginFactory));
            server.createContext("/plugin/unInstall", new PluginUnInstallHandler(pluginFactory));
            server.createContext("/plugin/active", new PluginActiveHandler(pluginFactory));
            server.createContext("/plugin/disable", new PluginDisableHandler(pluginFactory));
            server.start();
        } catch (Exception e) {
            logger.error("register enhance plugin center failure:" + e.getMessage(), e);
        }
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static String getApplicationHost() {
        return applicationHost;
    }

    public static String getEpRegistry() {
        return epRegistry;
    }
}
