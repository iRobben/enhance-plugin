package com.zrh.enhance.plugin.client.http;

import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zrh.enhancer.plugin.core.PluginDefinition;
import com.zrh.enhancer.plugin.core.PluginFactory;
import com.zrh.enhancer.plugin.dto.CommonResult;
import com.zrh.enhancer.plugin.dto.ResultCode;
import com.zrh.enhancer.plugin.exception.PluginOperateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

import static com.alibaba.fastjson.util.IOUtils.UTF8;


/**
 *
 * 安装插件
 * @author zhangrh
 */
public class PluginInstallHandler implements HttpHandler {

    private PluginFactory pluginFactory;

    public PluginInstallHandler(PluginFactory pluginFactory) {
        this.pluginFactory = pluginFactory;
    }

    @Override
    public void handle(HttpExchange exchange) {
        OutputStream os = exchange.getResponseBody();;
        try {
            exchange.sendResponseHeaders(200,0);
            //获得表单提交数据(post)
            String postString = IOUtils.toString(exchange.getRequestBody());
            PluginDefinition pluginDefinition = JSON.parseObject(postString, PluginDefinition.class);
            pluginFactory.installPlugin(pluginDefinition);
            CommonResult<String> commonResult = CommonResult.success(StringUtils.EMPTY);
            String response = JSON.toJSONString(commonResult);
            os.write(response.getBytes(UTF8));
        }catch (PluginOperateException e1){
            CommonResult<String> commonResult = CommonResult.failed(e1.getMessage());
            String response = JSON.toJSONString(commonResult);
            try {
                os.write(response.getBytes(UTF8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            CommonResult<String> commonResult = CommonResult.failed(ResultCode.EXCEPTION);
            String response = JSON.toJSONString(commonResult);
            try {
                os.write(response.getBytes(UTF8));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}