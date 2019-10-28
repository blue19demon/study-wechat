package com.starter.runner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *  * spring boot 容器加载完成后执行
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext ac = event.getApplicationContext();
		if (event.getApplicationContext().getParent() == null) {
              //只有root application context 没有父容器
			Thread thread = new Thread(ac.getBean(StepExecutor.class));
			thread.start();
		}
	}
}