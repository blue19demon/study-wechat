package com.wechat.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	// 应用ID,您的APPID
	private String AppID = "wx4492fde581248a1d";
	// AppSecret
	private String AppSecret = "a5a91cdbc9b737b028e8bd95d7ed65c0";
	// Token
	private String Token = "qixingbao";
	// host
	private String host = "http://w8qnby.natappfree.cc";
	// callback_uri
	private String callback_uri = "http://w8qnby.natappfree.cc/unifiedOrder";
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
