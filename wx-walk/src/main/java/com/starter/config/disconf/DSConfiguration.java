package com.starter.config.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Configuration
@DisconfFile(filename = "ds.properties")
public class DSConfiguration {

	private String type;

	private String driverClassName;
	
	private String url;
	
	private String username;
	
	private String password;
	@DisconfFileItem(name = "ds.type", associateField = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@DisconfFileItem(name = "ds.driverClassName", associateField = "driverClassName")
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	@DisconfFileItem(name = "ds.url", associateField = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@DisconfFileItem(name = "ds.username", associateField = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@DisconfFileItem(name = "ds.password", associateField = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
