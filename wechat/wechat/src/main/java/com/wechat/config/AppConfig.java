package com.wechat.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	// 应用ID,您的APPID
	private String AppID = "wx90afd5a95cf62c57";
	// AppSecret
	private String AppSecret = "73071d926b9174529eb433010f1d3586";
	// Token
	private String Token = "imook";
	// host
	private String host = "http://x3kw28.natappfree.cc";
	// callback_uri
	private String callback_uri = host+"/unifiedOrder";
	// EncodingAESKey
	private String EncodingAESKey = "pAmEKBy2mIJpYfZRenV3A3qclVi50CsOHVv2hGCC9bd";

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
