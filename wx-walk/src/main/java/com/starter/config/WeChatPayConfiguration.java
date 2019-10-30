package com.starter.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binarywang.spring.starter.wxjava.pay.properties.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.starter.config.disconf.WxPayConfiguration;

import lombok.AllArgsConstructor;

@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WeChatPayConfiguration {
	@Autowired
    private WxPayConfiguration wxPayConfiguration;
	@Bean
	@ConditionalOnMissingBean
	public WxPayService wxService() {
		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(StringUtils.trimToNull(wxPayConfiguration.getAppId()));
		payConfig.setMchId(StringUtils.trimToNull(wxPayConfiguration.getMchId()));
		payConfig.setMchKey(StringUtils.trimToNull(wxPayConfiguration.getMchKey()));

		// 可以指定是否使用沙箱环境
		payConfig.setUseSandboxEnv(true);

		WxPayService wxPayService = new WxPayServiceImpl();
		wxPayService.setConfig(payConfig);
		return wxPayService;
	}

}