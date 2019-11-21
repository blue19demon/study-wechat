package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "ds")
public class DSConfiguration {
	private String type;
	private String driverClassName;
	private String url;
	private String username;
	private String password;
}
