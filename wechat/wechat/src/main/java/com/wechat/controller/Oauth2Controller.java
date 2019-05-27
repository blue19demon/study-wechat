package com.wechat.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.AuthResult;
import com.wechat.config.WechatParamConfig;
import com.wechat.config.WechatAPIURL;
import com.wechat.utils.AuthUtil;
import com.wechat.utils.CheckoutUtil;
import com.wechat.utils.RedisUtils;

/**
 * 微信授权登录
 * 
 * 1 第一步：用户同意授权，获取code
 * 
 * 2 第二步：通过code换取网页授权access_token
 * 
 * 3 第三步：刷新access_token（如果需要）
 * 
 * 4 第四步：拉取用户信息(需scope为 snsapi_userinfo)
 * 
 * 5 附：检验授权凭证（access_token）是否有效
 * 
 * @author Administrator
 *
 */
@Controller
public class Oauth2Controller {
	private final static Logger logger = LoggerFactory.getLogger(Oauth2Controller.class);

    @Autowired
	private WechatParamConfig appConfig;
	@Autowired
	private AuthUtil authUtil;
	
	/**
	 * 微信消息接收和token验证
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/ownerCheck")
	@ResponseBody
	public String ownerCheck(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("进入验证服务器方法");
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		if (isGet) {
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			// 随机字符串
			String echostr = request.getParameter("echostr");
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
				return echostr;
			}
		}
		return null;
	}

	@RequestMapping("wx_login")
	public String wx_login() {
		return "wx_login";
	}
	
	@RequestMapping("wx_forbid_fun")
	public String wx_forbid_fun() {
		return "wx_forbid_fun";
	}

	@RequestMapping("wxAuth")
	public String wxAuth(String scope) {
		try {
			String auth_login = WechatAPIURL.get_code;
			auth_login = auth_login.replaceAll("APPID", appConfig.getAppID())
					.replaceAll("REDIRECT_URI", URLEncoder.encode(appConfig.getCallback_uri(), "UTF-8"))
					.replaceAll("SCOPE", scope);
			return "redirect:" + auth_login;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping("wxAuthallBack")
	public String wxAuthallBack(String code,Model model) {
		try {
			Map<String,Object> doGetAccessToken = authUtil.doGetAccessToken(code);
			logger.info(JSONObject.toJSONString(doGetAccessToken));
			String token=(String) doGetAccessToken.get("access_token");
			String openid=(String) doGetAccessToken.get("openid");
			Integer expires_in= doGetAccessToken.get("expires_in")!=null?Integer.parseInt(String.valueOf(doGetAccessToken.get("expires_in"))):0;
			
			AuthResult authResult=null;
			String user_info_json=RedisUtils.read("user_info_"+openid);
			if(user_info_json != null && !"".equals(user_info_json)) {
				logger.info("从缓冲获取user——info");
				authResult=JSONObject.parseObject(user_info_json, AuthResult.class);
			}else {
				logger.info("从网络获取user——info");
				String user_info = WechatAPIURL.get_user_info;
				user_info = user_info.replaceAll("ACCESS_TOKEN", token).replaceAll("OPENID", openid);
				JSONObject userInfo = authUtil.doGetJson(user_info);
				/**
				 * {
						"country": "中国",
						"province": "四川",
						"city": "成都",
						"openid": "o0BJQ5uR7L6QjrwRek8sklsuOXpc",
						"sex": 1,
						"nickname": "蓝精灵",
						"headimgurl": "http://thirdwx.qlogo.cn/mmopen/vi_32/Q2pWSGI7lwd7TusEp749OsgWlxosE1zzH4xKgvJt1KLbvoicQB5rZLG7lMFH2dzpUOMvEaiaRcMOuiczbia4brS0pg/132",
						"language": "zh_CN",
						"privilege": []
					}
				 */
				RedisUtils.write("user_info_"+openid, userInfo.toJSONString(),expires_in-10);
				authResult=JSONObject.parseObject(JSONObject.toJSONString(userInfo), AuthResult.class);
			}
			
			model.addAttribute("authResult", authResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login_result";
	}
}
