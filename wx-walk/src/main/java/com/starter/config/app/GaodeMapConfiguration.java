package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "gaode.api")
public class GaodeMapConfiguration {
	private String webkey;
	private String aroundSearchUrl;
}
