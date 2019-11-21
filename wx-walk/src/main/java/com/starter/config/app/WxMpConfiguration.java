package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "mp")
public class WxMpConfiguration {

	private String appId;

	private String secret;

	private String token;
	
}
