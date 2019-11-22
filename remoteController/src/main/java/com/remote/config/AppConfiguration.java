package com.remote.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
	private String tencentCloudIp;
	private String tencentCloudUsername;
	private String tencentCloudPasswd;
}
