package com.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfiguration {

	private String serverUrl;

	private String teplateAuthSuccessMsg;

	private String tulingKey;

	private String tulingAPI;

	private String mobileQueryAPI;

	private String weatherQueryKey;

	private String weatherQueryAPI;

	private String jokeKey;

	private String jokeAPI;

	private String idcardKey;

	private String idcardAPI;
}
