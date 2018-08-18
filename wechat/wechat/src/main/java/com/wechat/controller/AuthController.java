package com.wechat.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.AppConfig;
import com.wechat.config.ReqURL;
import com.wechat.utils.AuthUtil;
import com.wechat.utils.CheckoutUtil;

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
public class AuthController {

	@Autowired
	private AppConfig appConfig;

	/**
	 * 微信消息接收和token验证
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/ownerCheck")
	@ResponseBody
	public String ownerCheck(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(111);
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

	@RequestMapping("wxAuth")
	public String wxAuth() {
		try {
			String auth_login = ReqURL.get_code;
			auth_login = auth_login.replaceAll("APPID", appConfig.getAppID())
					.replaceAll("REDIRECT_URI", URLEncoder.encode(appConfig.getCallback_uri(), "UTF-8"))
					.replaceAll("SCOPE", "snsapi_userinfo");
			return "redirect:" + auth_login;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping("wxAuthallBack")
	public String wxAuthallBack(String code) {
		try {
			String access_token = ReqURL.get_access_token;
			access_token = access_token.replaceAll("APPID", appConfig.getAppID())
					.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
			JSONObject jsonObject = AuthUtil.doGetJson(access_token);
			String openid = jsonObject.getString("openid");
			String token = jsonObject.getString("access_token");
			String user_info = ReqURL.get_user_info;
			user_info = user_info.replaceAll("ACCESS_TOKEN", token).replaceAll("OPENID", openid);
			JSONObject userInfo = AuthUtil.doGetJson(user_info);
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
			System.out.println(JSONObject.toJSON(userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "wxAuthallBack";
	}
}
