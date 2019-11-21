package com.starter.api.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.app.PlatformAPIConfig;

@Component
public class ApiIdcard extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public String buildURL(String idcard) {
		return String.format(platformAPIConfig.getIdcardAPI(), platformAPIConfig.getIdcardKey(), idcard);
	}

	@Override
	public ApiManifest support() {
		return ApiManifest.IDCARD;
	}

	@Override
	public String handleSuccess(JSONObject resultJson) {
		JSONObject result = resultJson.getJSONObject("result");
		StringBuffer buffer = new StringBuffer();
		buffer.append("地区:").append(result.getString("area")).append("\n");
		buffer.append("性别:").append(result.getString("sex")).append("\n");
		buffer.append("出生日期:").append(result.getString("birthday")).append("\n");
		return buffer.toString();
	}
	@Override
	public Boolean isJuheAPI() {
		return Boolean.TRUE;
	}
}
