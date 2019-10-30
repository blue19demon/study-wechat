package com.starter.api.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.ApiStrategy;
import com.starter.config.disconf.PlatformAPIConfig;
import com.starter.utils.MD5;

@Component
public class ApiBaiduTrans extends ApiStrategy {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;

	@Override
	public Boolean isJuheAPI() {
		return Boolean.FALSE;
	}

	@Override
	public String buildURL(String query) {
		// 随机数
		String salt = String.valueOf(System.currentTimeMillis());

		// 签名
		String src = platformAPIConfig.getTransAppid() + query + salt + platformAPIConfig.getTransSecurityKey(); // 加密前的原文

		String to = checkAllIsCN(query) ? "en" : "zh";
		return String.format(platformAPIConfig.getTransApiUrl(), query, to, platformAPIConfig.getTransAppid(), salt,
				MD5.md5(src));
	}

	private boolean checkAllIsCN(String name) {
		int n = 0;
		for (int i = 0; i < name.length(); i++) {
			n = (int) name.charAt(i);
			if (!(19968 <= n && n < 40869)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean success(JSONObject resultJson) {
		JSONArray trans_result = resultJson.getJSONArray("trans_result");
		return trans_result != null && trans_result.size() > 0;
	}

	@Override
	public String handleSuccess(JSONObject resultJson) {
		JSONArray trans_result = resultJson.getJSONArray("trans_result");
		return trans_result.getJSONObject(0).getString("dst");
	}

	@Override
	public ApiManifest support() {
		return ApiManifest.BAIDU_TRANS;
	}

}
