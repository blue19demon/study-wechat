package com.starter.api.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ApiManifest {
	TULING("自动回复"),
	MOBILE_QUERY("手机号归属地"),
	WEATHER_QUERY("天气查询"),
	JOKE("开心一刻"),
	IDCARD("身份证识别"),
	HIS_TODAY("历史上的今天"),
	BAIDU_TRANS("百度翻译"),
	WEIXIN_QUERY("微信精选"),
	TOUTIAO("新闻头条"),
	HIS_TODAY_NEW("历史上的今天(新)"),
	TODAY_VIDEO("视频推荐");
	private String desc;

	public String getDesc() {
		return desc;
	}
}
