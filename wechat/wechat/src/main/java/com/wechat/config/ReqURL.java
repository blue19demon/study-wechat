package com.wechat.config;

/**
 * API地址
 * @author Administrator
 *
 */
public interface ReqURL {

	/**
	 * 获取code
	 */
	public static final String get_code="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	/**
	 * 获取access_token
	 */
	public static final String get_access_token="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 获取user_info
	 */
	public static final String get_user_info="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
}
