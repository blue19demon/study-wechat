package com.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "baidu.api")
@Configuration
public class BaiduApiConfiguration {
	
	 private String mapAk;
	 
	 private String mapUrlPlaceSearch;

	 private String mapUrlGetLocation;
	 
	 private String mapConvertUrl;
	
}
