package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "pay")
public class WxPayConfiguration {

	private String appId;

	private String mchId;

	private String mchKey;
	
}
