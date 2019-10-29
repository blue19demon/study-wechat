package com.starter.api.platform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.PlatformAPIConfig;
@Component
public class ApiHisToday extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public ApiManifest support() {
		return ApiManifest.HIS_TODAY;
	}

	@Override
	public String buildURL(String content) {
		SimpleDateFormat smf = new SimpleDateFormat("MM-dd");
		Date dt = null;
		try {
			dt = smf.parse(content);
		} catch (ParseException e) {
			e.printStackTrace();
			return "请输入正确日期";
		}

		String mon = String.format("%tm", dt);

		String day = String.format("%td", dt);
		String apiUrl = String.format(platformAPIConfig.getHisTodayAPI(),platformAPIConfig.getHisTodayKey(),
				mon, day);
		return apiUrl;
	}


	@Override
	public String handleSuccess(JSONObject resultJson) {
		JSONArray result = resultJson.getJSONArray("result");
		StringBuffer buffer = new StringBuffer();
		if (result != null && result.size() > 0) {
			buffer.append("----------------------------\n");
			for (int i = 0; i < result.size(); i++) {
				if(i>=5) {
					break;
				}
				JSONObject item = result.getJSONObject(i);
				buffer.append(item.getString("lunar")).append("\n");
				buffer.append(item.getString("title")).append("\n");
				buffer.append(item.getString("des")).append("\n");
				if (i != result.size() - 1) {
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
