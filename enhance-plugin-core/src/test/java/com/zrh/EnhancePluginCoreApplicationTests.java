package com.zrh;

import com.zrh.enhancer.plugin.core.DefaultPluginFactory;
import com.zrh.enhancer.plugin.core.PluginDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnhancePluginCoreApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	private DefaultPluginFactory factory;
	
	@Before
	public void init() {
		factory = new DefaultPluginFactory();
	}
	
	
	// 本地安装测试
	@Test
	public void intallTest() {
		PluginDefinition config = new PluginDefinition();
		config.setIsActive(false);
		config.setId("2");
		config.setJarRemoteUrl("file:/Users/zhangrenhua/.m2/repository/com/zrh/enhance-plugin-repository/0.0.1-SNAPSHOT/enhance-plugin-repository-0.0.1-SNAPSHOT.jar");
		config.setClassName("com.zrh.enhance.plugin.repository.CountingBeforeAdvice");
		config.setName("服务执行统计");
		factory.installPlugin(config);
	}

}
