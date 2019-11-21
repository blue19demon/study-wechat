package com.starter.api.platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.app.PlatformAPIConfig;
@Component
public class ApiMobileQuery extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Override
	public String doValidate(String mobile) {
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
		return null;
	}
	
	@Override
	public String buildURL(String mobile) {
		return  String.format(platformAPIConfig.getMobileQueryAPI(), mobile);
	}

	@Override
	public ApiManifest support() {
		return ApiManifest.MOBILE_QUERY;
	}

	@Override
	public Boolean success(JSONObject resultJson) {
		return resultJson != null;
	}

	@Override
	public String handleFail(JSONObject resultJson) {
		return "未知电话";
	}

	@Override
	public String handleSuccess(JSONObject resultJson) {
		return resultJson.getString("catName").concat("(" + resultJson.getString("province") + ")");
	}
	@Override
	public Boolean isJuheAPI() {
		return Boolean.FALSE;
	}
}
