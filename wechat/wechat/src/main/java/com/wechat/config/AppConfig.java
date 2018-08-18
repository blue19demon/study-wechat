package com.wechat.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	// 应用ID,您的APPID
	private String AppID = "wx0985b6dbaeeff28c";
	// AppSecret
	private String AppSecret = "d3ba2d045ffc578abd0e9d23f8c93404";
	// Token
	private String Token = "imook";
	// callback_uri
	private String callback_uri = "http://szhvh2.natappfree.cc/wxAuthallBack";
	// EncodingAESKey
	private String EncodingAESKey = "hYc4MGwy79rGqyuTO3uZL3vQjcsC7vGet3m0IxVhwGp";
	public String getAppID() {
		return AppID;
	}
	public void setAppID(String appID) {
		AppID = appID;
	}
	public String getAppSecret() {
		return AppSecret;
	}
	public void setAppSecret(String appSecret) {
		AppSecret = appSecret;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	
	public String getCallback_uri() {
		return callback_uri;
	}
	public void setCallback_uri(String callback_uri) {
		this.callback_uri = callback_uri;
	}
	public String getEncodingAESKey() {
		return EncodingAESKey;
	}
	public void setEncodingAESKey(String encodingAESKey) {
		EncodingAESKey = encodingAESKey;
	}

}
