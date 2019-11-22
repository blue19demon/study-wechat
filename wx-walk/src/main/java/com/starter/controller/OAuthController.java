package com.starter.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.starter.config.app.AliAPIConfiguration;
import com.starter.config.app.AppConfiguration;
import com.starter.pojo.AuthResult;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

@Controller
@Slf4j
public class OAuthController {
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;
	@Autowired
	private AliAPIConfiguration aliAPIConfiguration;// 支付宝sdk配置
	@Autowired
	private AlipayClient client;// 支付宝请求sdk客户端

	@RequestMapping("/oauth")
	public String oauth() {
		return "oauth";
	}

	@RequestMapping("/wechatAuthorize")
	public void wechatAuthorize(HttpServletResponse response) {
		// 构造oauth2授权的url连接.
		// 参数一：用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
		// 参数二:scope
		// 参数三:state，重定向后会带上state参数
		String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(appConfiguration.getServerUrl() + "/userInfo",
				WxConsts.OAuth2Scope.SNSAPI_USERINFO, "WECHAT");
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/aliAuthorize")
	public void aliAuthorize(HttpServletResponse response) {
		// 构造oauth2授权的url连接.
		// 参数一：用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
		// 参数二:scope
		// 参数三:state，重定向后会带上state参数
		String redirectUrl = String.format(aliAPIConfiguration.getAuthGateWay(),aliAPIConfiguration.getAppId(),
				appConfiguration.getServerUrl() + "/userInfo", "ALI");
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/userInfo")
	public String userInfo(Model model,HttpServletRequest req)
			throws WxErrorException {
		String state=req.getParameter("state");
		log.error("【网页授权】state={}", state);
		AuthResult authResult = null;
		if ("WECHAT".equals(state)) {
			String code=req.getParameter("code");
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
			try {
				// 用code换取oauth2的access token,openId等信息
				wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			} catch (WxErrorException e) {
				log.error("【微信网页授权】{}", e);
			}
			WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			authResult = AuthResult.builder()
					.account(wxMpUser.getOpenId())
					.nickname(wxMpUser.getNickname())
					.headImgUrl(wxMpUser.getHeadImgUrl())
					.sexDesc(wxMpUser.getSexDesc())
					.country(wxMpUser.getCountry())
					.province(wxMpUser.getProvince())
					.city(wxMpUser.getCity())
					.build();
			
			model.addAttribute("openId", wxMpUser.getOpenId());
		}else {
			try {
				String code=req.getParameter("auth_code");
				authResult = this.generateAli(code);
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("state", state);
		model.addAttribute("authResult", authResult);
		log.info(JSONObject.toJSONString(authResult, true));
		return "oauthReturn";
	}

	private AuthResult generateAli(String code) throws AlipayApiException {
		// 获取用户信息授权
		AlipaySystemOauthTokenRequest requestToken = new AlipaySystemOauthTokenRequest();
		requestToken.setCode(code);
		requestToken.setGrantType("authorization_code");
		AlipaySystemOauthTokenResponse oauthTokenResponse = client.execute(requestToken);
		log.info("<br/>AccessToken:" + oauthTokenResponse.getAccessToken() + "\n");
		AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
		AlipayUserInfoShareResponse userinfoShareResponse = client.execute(requestUser,
				oauthTokenResponse.getAccessToken());
		log.info("userinfoShareResponse"+JSONObject.toJSONString(userinfoShareResponse, true));
		AuthResult authResult = AuthResult.builder()
				.account(userinfoShareResponse.getUserId())
				.nickname(userinfoShareResponse.getUserName())
				.headImgUrl(userinfoShareResponse.getAvatar())
				.sexDesc("f".equals(userinfoShareResponse.getGender())?"女":"男")
				.country(userinfoShareResponse.getCountryCode())
				.province(userinfoShareResponse.getProvince())
				.city(userinfoShareResponse.getCity())
				.build();
		return authResult;
	}

	@RequestMapping("/sendTempMsg")
	@ResponseBody
	public void sendTempMsg(Model model, @RequestParam("formatted_address") String formatted_address,
			@RequestParam("openId") String openId) throws WxErrorException {
		WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		templateMessage.setToUser(wxMpUser.getOpenId());
		templateMessage.setUrl(appConfiguration.getServerUrl() + "/myLocation");
		templateMessage.setTemplateId(appConfiguration.getTeplateAuthSuccessMsg());
		templateMessage.setData(Arrays.asList(new WxMpTemplateData("title", "您在【" + formatted_address + "】授权成功"),
				new WxMpTemplateData("name", wxMpUser.getNickname()),
				new WxMpTemplateData("login_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
		wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
	}

}
