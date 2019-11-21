package com.starter.api.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.app.PlatformAPIConfig;

@Component
public class ApiJoke extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public String buildURL(String content) {
		long timeStampSec = System.currentTimeMillis() / 1000;
		String apiUrl = String.format(platformAPIConfig.getJokeAPI(), String.format("%010d", timeStampSec),
				platformAPIConfig.getJokeKey());
		return apiUrl;
	}

	@Override
	public ApiManifest support() {
		return ApiManifest.JOKE;
	}

	@Override
	public String handleSuccess(JSONObject resultJson) {
		JSONObject result = resultJson.getJSONObject("result");
		StringBuffer buffer = new StringBuffer();
		JSONArray data = result.getJSONArray("data");
		if (data != null && data.size() > 0) {
			buffer.append("----------------------------\n");
			for (int i = 0; i < data.size(); i++) {
				JSONObject item = data.getJSONObject(i);
				buffer.append(item.getString("content")).append("\n");
				if (i != data.size() - 1) {
					buffer.append("----------------------------\n");
				}
			}
		}
		return buffer.toString();
	}
	@Override
	public Boolean isJuheAPI() {
		return Boolean.TRUE;
	}
}
