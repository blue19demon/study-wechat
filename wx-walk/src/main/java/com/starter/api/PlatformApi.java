package com.starter.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.config.AppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlatformApi {
	@Autowired
	private AppConfiguration appConfiguration;
	@Autowired
	private RestTemplate restTemplate;

	public String searchMobile(String mobile) throws Exception {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		if (mobile.length() != 11) {
			return "手机号应为11位数";
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(mobile);
			boolean isMatch = m.matches();
			if (!isMatch) {
				return "您的手机号错误格式！！！";
			}
		}
		String apiUrl = String.format(appConfiguration.getMobileQueryAPI(), mobile);
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info(result);
		JSONObject jsonResult = JSONObject.parseObject(result.substring("__GetZoneResult_ = ".length()));
		if (jsonResult != null) {
			return jsonResult.getString("catName").concat("(" + jsonResult.getString("province") + ")");
		}
		return "未知电话";
	}
	
	public String searchWeather(String city) throws Exception {
		String apiUrl = String.format(appConfiguration.getWeatherQueryAPI(), city,appConfiguration.getWeatherQueryKey());
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info(result);
		JSONObject object = JSONObject.parseObject(result);
        if(object.getInteger("error_code")==0){
        	JSONObject resultJson = object.getJSONObject("result");
        	return formatOut(resultJson);
        }else{
        	return object.get("error_code")+":"+object.get("reason");
        }
	}

	private String formatOut(JSONObject resultJson) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("查询结果").append("\n\n");
		buffer.append("城市:").append(resultJson.getString("city")).append("\n\n\n");
		JSONObject realtime = resultJson.getJSONObject("realtime");
		buffer.append("当前天气详情情况:").append("\n");
		buffer.append("天气情况:").append(realtime.getString("info")).append("\n");
		buffer.append("温度:").append(realtime.getString("temperature")).append("℃\n");
		buffer.append("湿度:").append(realtime.getString("humidity")).append("\n");
		buffer.append("风向:").append(realtime.getString("direct")).append("\n");
		buffer.append("风力:").append(realtime.getString("power")).append("\n");
		buffer.append("空气质量指数:").append(realtime.getString("aqi")).append("\n");
		JSONArray futures = resultJson.getJSONArray("future");
		if(futures!=null&&futures.size()>0) {
			buffer.append("近5天天气情况:").append("\n\n\n");
			buffer.append("-------------------\n");
			for (int i = 0; i < futures.size(); i++) {
				JSONObject future = futures.getJSONObject(i);
				buffer.append("日期:").append(future.getString("date")).append("\n");
				buffer.append("温度:").append(future.getString("temperature")).append("\n");
				buffer.append("天气情况:").append(future.getString("weather")).append("\n");
				buffer.append("风向:").append(future.getString("direct")).append("\n");
				if(i!=futures.size()-1) {
					buffer.append("-------------------\n");
				}
			}
		}
		return buffer.toString();
	}

	public String joke() {
		long timeStampSec = System.currentTimeMillis()/1000;
		String apiUrl = String.format(appConfiguration.getJokeAPI(),String.format("%010d", timeStampSec),appConfiguration.getJokeKey());
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info(result);
		JSONObject object = JSONObject.parseObject(result);
        if(object.getInteger("error_code")==0){
        	JSONObject resultJson = object.getJSONObject("result");
        	return formatJoke(resultJson);
        }else{
        	return object.get("error_code")+":"+object.get("reason");
        }
	}

	private String formatJoke(JSONObject resultJson) {
		StringBuffer buffer = new StringBuffer();
		JSONArray data = resultJson.getJSONArray("data");
		if(data!=null&&data.size()>0) {
			buffer.append("----------------------------\n");
			for (int i = 0; i < data.size(); i++) {
				JSONObject item = data.getJSONObject(i);
				buffer.append(item.getString("content")).append("\n");
				if(i!=data.size()-1) {
					buffer.append("----------------------------\n");
				}
			}
		}
		return buffer.toString();
	}

	public String idcard(String idcard) {
		String apiUrl = String.format(appConfiguration.getIdcardAPI(),appConfiguration.getIdcardKey(),idcard);
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info(result);
		JSONObject object = JSONObject.parseObject(result);
		JSONObject resultJson = object.getJSONObject("result");
        if(resultJson!=null){
        	return formatIdcard(resultJson);
        }
		return "出错了";
	}

	private String formatIdcard(JSONObject result) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("地区:").append(result.getString("area")).append("\n");
		buffer.append("性别:").append(result.getString("sex")).append("\n");
		buffer.append("出生日期:").append(result.getString("birthday")).append("\n");
		return buffer.toString();
	}
}
