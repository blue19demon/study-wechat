package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "baidu.api")
public class BaiduMapConfiguration {
	private String mapAk;
	private String mapUrlPlaceSearch;
	private String mapUrlGetLocation;
	private String mapConvertUrl;
}
