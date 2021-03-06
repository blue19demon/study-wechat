package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
@ConfigurationProperties(prefix = "platform.api")
public class PlatformAPIConfig {
	private String tulingKey;
	private String tulingAPI;
	private String mobileQueryAPI;
	private String weatherQueryKey;
	private String weatherQueryAPI;
	private String jokeKey;
	private String jokeAPI;
	private String idcardKey;
	private String idcardAPI;
	private String hisTodayKey;
	private String hisTodayAPI;
	private String transApiUrl;
	private String transAppid;
	private String transSecurityKey;
	private String weixinQueryKey;
	private String weixinQueryAPI;
	private String toutiaoKey;
	private String toutiaoAPI;
	private String musicAPI;
	private String vedioSearchAPI;
	private String vedioDetailAPI;
	private String hisTodayNewAPI;
	private String satinApi;
	
}
