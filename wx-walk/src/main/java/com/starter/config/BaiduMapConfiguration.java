package com.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "baidu.map")
@Configuration
public class BaiduMapConfiguration {
	
	 private String ak;
	 
	 private String urlPlaceSearch;
	 
	 private String convertUrl;
	 
	 private String urlGetLocation;
}
