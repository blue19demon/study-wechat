package com.wechat.servcie;

import java.util.Map;

public interface WeixinService {
	/**
	 * @Title: weixinjsIntefaceSign
	 * @Description: 微信js接口授权
	 * @param map
	 * @return
	 * @return: Map<String,String>
	 */
	public Map<String, String> weixinjsIntefaceSign(Map<String, String> map);

}
