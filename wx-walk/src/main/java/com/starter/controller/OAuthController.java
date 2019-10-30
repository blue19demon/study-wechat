package com.starter.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.starter.config.disconf.AppConfiguration;

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
	@RequestMapping("/authorize")
	public void authorize(HttpServletResponse response) {
		// 构造oauth2授权的url连接.
		// 参数一：用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
		// 参数二:scope
		// 参数三:state，重定向后会带上state参数
		String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(appConfiguration.getServerUrl()+"/userInfo", WxConsts.OAuth2Scope.SNSAPI_USERINFO, "aa");
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/userInfo")
	public String userInfo(Model model,@RequestParam("code") String code, @RequestParam("state") String state)
			throws WxErrorException {
		log.error("【微信网页授权】state={}", state);
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
		try {
			// 用code换取oauth2的access token,openId等信息
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			log.error("【微信网页授权】{}", e);
		}
		WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		model.addAttribute("wxMpUser", wxMpUser);
		log.info(JSONObject.toJSONString(wxMpUser, true));
		return "oauth";
	}
	
	@RequestMapping("/sendTempMsg")
	@ResponseBody
	public void sendTempMsg(Model model,@RequestParam("formatted_address") String formatted_address, @RequestParam("openId") String openId)
			throws WxErrorException {
		WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		templateMessage.setToUser(wxMpUser.getOpenId());
		templateMessage.setUrl(appConfiguration.getServerUrl()+"\\mapLocation");
		templateMessage.setTemplateId(appConfiguration.getTeplateAuthSuccessMsg());
		templateMessage.setData(Arrays.asList(
				new WxMpTemplateData("title", "您在【" + formatted_address+ "】授权成功"),
				new WxMpTemplateData("name", wxMpUser.getNickname()),
				new WxMpTemplateData("login_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
		wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
	}
	
}
