package com.zrh.enhancer.plugin.core;

import com.zrh.enhancer.plugin.exception.PluginOperateException;
import org.aopalliance.aop.Advice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrenhua
 * @title 默认插件工厂实现
 * @desc
 * @date 2019/9/15
 */


public class DefaultPluginFactory implements PluginFactory {
    
    /**
     * 缓存已经安装的插件定义组件信息
     * <id, PluginDefinition>
     * @see PluginDefinition
     */
    private Map<String,PluginDefinition> cachePluginDefinitionMap = new ConcurrentHashMap<>();
    
    /**
     * 缓存已经实例化的通知类组件信息
     * <id, PluginDefinition>
     * @see PluginDefinition
     */
    private Map<String,Advice> cacheAdviceMap = new ConcurrentHashMap<>();
    
    private static final String BASE_DIR;
    
    static {
        BASE_DIR = System.getProperty("user.home") + "/.plugins/";
    }
    
    
    @Override
    public void installPlugin(PluginDefinition pluginDefinition) {
        if(pluginDefinition == null){
            throw new PluginOperateException(-1L,"获取插件定义组件失败");
        }
        //缓存插件信息
        String id = pluginDefinition.getId();
        cachePluginDefinitionMap.putIfAbsent(id, pluginDefinition);
        
        // 1.从远端下载插件jar包
        String className = pluginDefinition.getClassName();
        if(cacheAdviceMap.get(className) != null){
            throw new PluginOperateException(-1L,"该插件已经安装过了");
        }
        String jarRemoteUrl = pluginDefinition.getJarRemoteUrl();
        File jarFile = new File(getLoadFileUrl(jarRemoteUrl));
        if(!jarFile.exists()){
            InputStream inputStream = null;
            try {
                jarFile.getParentFile().mkdirs();
                URL jarUrl = new URL(jarRemoteUrl);
                inputStream = jarUrl.openStream();
                Files.copy(inputStream,jarFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
                jarFile.deleteOnExit();
                throw new PluginOperateException(-1L,"插件下载失败");
            } finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        // 2.把jar包加载到classLoader
        URLClassLoader urlClassLoader = (URLClassLoader)getClass().getClassLoader();
        boolean isLoader = true;
        try {
            URL targetUrl = jarFile.toURI().toURL();
            for(URL url : urlClassLoader.getURLs()){
                if (url.equals(targetUrl)) {
                    isLoader = false;
                    break;
                }
            }
            //利用反射将url加载到classLoader
            if(isLoader){
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(urlClassLoader,targetUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PluginOperateException(-1L,"插件加载失败");
        }
        
        // 3.实例化插件对象
        Class<?> adviceClass = null;
        try {
            adviceClass = urlClassLoader.loadClass(className);
            if(! Advice.class.isAssignableFrom(adviceClass)){
                throw new PluginOperateException(-1L,"插件实现类错误");
            }
            Advice advice = (Advice)adviceClass.newInstance();
            cacheAdviceMap.put(className,advice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PluginOperateException(-1L,"插件类实例化失败");
        }
    
        // 4.本地持久化
        
    
    
    }
    
    private String getLoadFileUrl(String jarRemoteUrl){
       return BASE_DIR + jarRemoteUrl.substring(jarRemoteUrl.lastIndexOf("/"));
    }
    
    /**
     * 下载插件
     * @param pluginDefinition
     */
    private void downPlugin(PluginDefinition pluginDefinition){
    
    
    }
    
    @Override
    public void unInstallPlugin(PluginDefinition pluginDefinition) {
    
    }
    
    @Override
    public void activePlugin(String pluginId) {
    
    }
    
    @Override
    public void disablePlugin(String pluginId) {
    
    }
    
    @Override
    public List<PluginDefinition> pluginList() {
        return null;
    }
}
