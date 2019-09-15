package com.zrh.enhancer.plugin.core;

import com.zrh.enhancer.plugin.exception.PluginOperateException;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrenhua
 * @title 默认插件工厂实现
 * @desc
 * @date 2019/9/15
 */


public class DefaultPluginFactory implements ApplicationContextAware,InitializingBean,PluginFactory {
    
    private static final String BASE_DIR;
    
    private static final String LOCAL_STORE_FILE = "pluginDefinitions.obj";
    
    /**
     * 应用上下文，类初始化后调用setApplicationContext
     */
    private ApplicationContext applicationContext;
    
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
        //下载远程插件,构建通知
        this.buildAdvice(pluginDefinition);
        //本地持久化
        this.localPersistence();
    }
    
    /**
     * 获取jar名称url
     * @param jarRemoteUrl
     * @return
     */
    private String getLoadFileUrl(String jarRemoteUrl){
       return BASE_DIR + jarRemoteUrl.substring(jarRemoteUrl.lastIndexOf("/"));
    }
    
    /**
     * 构建通知
     * @param pluginDefinition
     * @return
     */
    private Advice buildAdvice(PluginDefinition pluginDefinition){
        // 1.从远端下载插件jar包
        String className = pluginDefinition.getClassName();
        if(cacheAdviceMap.get(className) != null){
            return cacheAdviceMap.get(className);
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
            return advice;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PluginOperateException(-1L,"插件类实例化失败");
        }
        
    }
    
    /**
     * 存储在本地
     */
    private void localPersistence() {
        try {
            File definitionFile = new File(BASE_DIR + LOCAL_STORE_FILE);
            if(!definitionFile.exists()){
                definitionFile.getParentFile().mkdirs();
                definitionFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(definitionFile);
            ObjectOutputStream output = new ObjectOutputStream(out);
            Map<String, PluginDefinition> pluginDefinitionMap = new HashMap<>();
            pluginDefinitionMap.putAll(cachePluginDefinitionMap);
            output.writeObject(pluginDefinitionMap);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new PluginOperateException(-1L,"插件定义本地缓存失败");
        }
    }
   
    
    @Override
    public void unInstallPlugin(PluginDefinition pluginDefinition) {
        //1.禁用指定插件
        this.disablePlugin(pluginDefinition.getId());
        //2.缓存中删除
        cachePluginDefinitionMap.remove(pluginDefinition.getId());
        //3.刷新本地存储
        this.localPersistence();
    }
    
    @Override
    public void activePlugin(String pluginId) {
        
        //1.获取插件定义信息
        PluginDefinition pluginDefinition = cachePluginDefinitionMap.get(pluginId);
        if(pluginDefinition == null){
            throw new PluginOperateException(-1L,"该插件还没有安装");
        }
        //2.获取通知
        Advice advice = this.buildAdvice(pluginDefinition);
        
        //3.添加通知到代理对象上
        for(String name : applicationContext.getBeanDefinitionNames()){
            Object bean = applicationContext.getBean(name);
            if(bean == this){
                continue;
            }
            if (!(bean instanceof Advised)) {
                continue;
            }
            //是否已经添加过
            Advice advice1 = this.findAdvice(pluginDefinition.getClassName(), (Advised) bean);
            if(advice1 != null){
                continue;
            }
            ((Advised)bean).addAdvice(advice);
        }
        
        //4. 更新状态，刷新本地存储
        pluginDefinition.setIsActive(true);
        this.localPersistence();
        
    
    }
    
    /**
     * 查找通知链里面是否已经添加过相同的通知,，如果有返回当前通知类
     * @param className
     * @param advised
     * @return
     */
    private Advice findAdvice(String className,Advised advised){
        for(Advisor advisor : advised.getAdvisors()){
            if(advisor.getAdvice().getClass().getName().equals(className)){
                return advisor.getAdvice();
            }
        }
        return null;
    }
    
    @Override
    public void disablePlugin(String pluginId) {
    
        //1.获取插件定义信息
        PluginDefinition pluginDefinition = cachePluginDefinitionMap.get(pluginId);
        if(pluginDefinition == null){
            throw new PluginOperateException(-1L,"该插件还没有安装");
        }
        //2.去除通知
        for(String name : applicationContext.getBeanDefinitionNames()){
            Object bean = applicationContext.getBean(name);
            if (bean instanceof Advised) {
                Advice advice = this.findAdvice(pluginDefinition.getClassName(), (Advised) bean);
                if(advice != null){
                    ((Advised)bean).removeAdvice(advice);
                }
            }
        }
        //3. 更新状态，刷新本地存储
        pluginDefinition.setIsActive(false);
        this.localPersistence();
    }
    
    @Override
    public List<PluginDefinition> pluginList() {
        Map<String, PluginDefinition> pluginDefinitionMap = this.readerLocalDefinitions();
        if(pluginDefinitionMap != null){
            return new ArrayList<>(pluginDefinitionMap.values());
        }
        return null;
    }
    
    
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.applicationContext = applicationContext;
    }
    
    
    /**
     * setApplicationContext方法执行后开始执行
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.loaderLocalPlugins();
    }
    
    /**
     * 装载本地已安装插件
     */
    private void loaderLocalPlugins(){
        try {
            Map<String, PluginDefinition> localDefinitions = this.readerLocalDefinitions();
            if(localDefinitions == null){
                return;
            }
            cachePluginDefinitionMap.putAll(localDefinitions);
            for(PluginDefinition pluginDefinition : localDefinitions.values()){
                if(pluginDefinition.getIsActive()){
                    this.activePlugin(pluginDefinition.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 读取本地配置
     * @return
     */
    private Map<String,PluginDefinition> readerLocalDefinitions(){
        FileInputStream fileInputStream = null;
        try {
            File definitionFile = new File(BASE_DIR + LOCAL_STORE_FILE);
            if (!definitionFile.exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(definitionFile);
            ObjectInputStream input = new ObjectInputStream(fileInputStream);;
            return (Map<String, PluginDefinition>)input.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           
        }
        return null;
    }
    
   
}
