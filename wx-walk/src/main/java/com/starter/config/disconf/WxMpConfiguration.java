package com.starter.config.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Configuration
@DisconfFile(filename = "wxMp.properties")
public class WxMpConfiguration {

	private String appId;

	private String secret;

	private String token;
	@DisconfFileItem(name = "mp.appId", associateField = "appId")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	@DisconfFileItem(name = "mp.secret", associateField = "secret")
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	@DisconfFileItem(name = "mp.token", associateField = "token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

	
}
