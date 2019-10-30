package com.starter.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;

/**
 * Disconf配置类
 */
@Configuration
public class DisconfConfig {
    @Bean(destroyMethod = "destroy")
    public DisconfMgrBean getDisconfMgrBean() {
        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage("com.starter.config.disconf");
        return disconfMgrBean;
    }

    @Bean(destroyMethod = "destroy", initMethod = "init")
    public DisconfMgrBeanSecond getDisconfMgrBean2() {
        return new DisconfMgrBeanSecond();
    }
}