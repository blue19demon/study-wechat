package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "redis")
public class RedisConfiguration {

	private String host;

	private String port;
}
