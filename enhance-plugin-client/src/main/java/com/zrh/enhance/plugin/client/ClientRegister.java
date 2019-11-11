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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 客户端注册
 * @author zhangrh
 */
public class ClientRegister {

    private static Logger logger = LoggerFactory.getLogger(ClientRegister.class);

    private static final String KEY_CONFIG_FILE = "config";

    private static final String KEY1 = "application_name";

    private static final String KEY2 = "ep_registry";

    private static final String KEY3 = "application_host";

    public ClientRegister(PluginFactory pluginFactory) {
        //获取配置文件属性
        ResourceBundle rb = ResourceBundle.getBundle(KEY_CONFIG_FILE, Locale.ENGLISH);
        try {
            //向管理平台注册应用信息
            String applicationName = rb.getString(KEY1);
            String epRegistry = rb.getString(KEY2);
            String applicationHost = rb.getString(KEY3);
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

}
