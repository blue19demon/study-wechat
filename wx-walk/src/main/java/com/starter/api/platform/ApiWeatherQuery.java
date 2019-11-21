package com.starter.api.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.app.PlatformAPIConfig;

@Component
public class ApiWeatherQuery extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public String buildURL(String content) {
		String apiUrl = String.format(platformAPIConfig.getWeatherQueryAPI(), content,
				platformAPIConfig.getWeatherQueryKey());
		return apiUrl;
	}

	@Override
	public ApiManifest support() {
		return ApiManifest.WEATHER_QUERY;
	}

	@Override
	public String handleSuccess(JSONObject resultJson) {
		JSONObject result = resultJson.getJSONObject("result");
		StringBuffer buffer = new StringBuffer();
		buffer.append("查询结果").append("\n\n");
		buffer.append("城市:").append(result.getString("city")).append("\n\n");
		JSONObject realtime = result.getJSONObject("realtime");
		buffer.append("当前天气详情情况:").append("\n");
		buffer.append("天气情况:").append(realtime.getString("info")).append("\n");
		buffer.append("温度:").append(realtime.getString("temperature")).append("℃\n");
		buffer.append("湿度:").append(realtime.getString("humidity")).append("\n");
		buffer.append("风向:").append(realtime.getString("direct")).append("\n");
		buffer.append("风力:").append(realtime.getString("power")).append("\n");
		buffer.append("空气质量指数:").append(realtime.getString("aqi")).append("\n\n\n");
		JSONArray futures = result.getJSONArray("future");
		if (futures != null && futures.size() > 0) {
			buffer.append("近5天天气情况:").append("\n");
			buffer.append("-------------------\n");
			for (int i = 0; i < futures.size(); i++) {
				JSONObject future = futures.getJSONObject(i);
				buffer.append("日期:").append(future.getString("date")).append("\n");
				buffer.append("温度:").append(future.getString("temperature")).append("\n");
				buffer.append("天气情况:").append(future.getString("weather")).append("\n");
				buffer.append("风向:").append(future.getString("direct")).append("\n");
				if (i != futures.size() - 1) {
					buffer.append("-------------------\n");
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
