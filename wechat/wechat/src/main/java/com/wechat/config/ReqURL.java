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
	 * 获取access_token的接口地址（GET） 限200（次/天）
	 */
	public static final String get_access_token="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 获取user_info
	 */
	public static final String get_user_info="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 发送模板消息POST
	 */
	public static final String send_template_msg="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/**
	 * 获取access_token的接口地址（GET） 限200（次/天）
	 */
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
	 * 自定义菜单创建接口
	 */
    public final static String create_menu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
	 * 自定义菜单删除接口
	 */
    public final static String delete_menu = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
   	 * 创建个性化菜单
   	 */
     public final static String addconditional_menu = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=ACCESS_TOKEN";
}
