package com.starter.config.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Configuration
@DisconfFile(filename = "wxPay.properties")
public class WxPayConfiguration {

	private String appId;

	private String mchId;

	private String mchKey;
	@DisconfFileItem(name = "pay.appId", associateField = "appId")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	@DisconfFileItem(name = "pay.mchId", associateField = "mchId")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	@DisconfFileItem(name = "pay.mchKey", associateField = "mchKey")
	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	
}
