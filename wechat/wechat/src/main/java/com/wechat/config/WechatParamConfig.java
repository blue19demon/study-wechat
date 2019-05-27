package com.wechat.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatParamConfig {
	// 应用ID,您的APPID  主账 wx90afd5a95cf62c57 测试账户wx0985b6dbaeeff28c
	private String AppID = "wx0985b6dbaeeff28c";
	// AppSecret
	//主账 acad3724efc8a745fe42b0b9554530f6 
	//测试账户 d3ba2d045ffc578abd0e9d23f8c93404
	private String AppSecret = "d3ba2d045ffc578abd0e9d23f8c93404";
	// Token
	private String Token = "imook";
	// host
	private String host = "http://iddda4.natappfree.cc";
	// callback_uri
	private String callback_uri = host+"/wxAuthallBack";
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
