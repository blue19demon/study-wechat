package com.starter.api.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.disconf.PlatformAPIConfig;
@Component
public class ApiHisTodayNew extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public ApiManifest support() {
		return ApiManifest.HIS_TODAY_NEW;
	}

	@Override
	public String buildURL(String content) {
		return platformAPIConfig.getHisTodayNewAPI();
	}


	@Override
	public String handleSuccess(JSONObject resultJson) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("历史上的今天\n\n");
		buffer.append(resultJson.getString("today")+"\n\n");
		JSONArray result = resultJson.getJSONArray("result");
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if(i>=10) {
					break;
				}
				JSONObject item = result.getJSONObject(i);
				buffer.append(item.getString("year")).append("年\n");
				buffer.append(item.getString("title")).append("\n");
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
	@Override
	public Boolean success(JSONObject resultJson) {
		return null != resultJson;
	}

	@Override
	public String handleFail(JSONObject resultJson) {
		return "待开发有点麻烦！\n";
	}
	@Override
	public Boolean isJuheAPI() {
		return Boolean.FALSE;
	}

}
