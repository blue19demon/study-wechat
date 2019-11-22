package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
	private String serverUrl;
	private String teplateAuthSuccessMsg;
	private String uploadFolder;
	/**
	 * 搜索范围（单位：米）
	 */
	private String radius;
	private String sysOpenId;
	private String remoteStart;
	private String remoteStop;
}
