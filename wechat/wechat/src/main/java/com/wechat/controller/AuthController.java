package com.wechat.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.AuthResult;
import com.wechat.bean.TemplateData;
import com.wechat.bean.WxTemplate;
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
	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	private AppConfig appConfig;
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
	public String wxAuthallBack(String code,Model model) {
		try {
			Map<String,Object> doGetAccessToken = authUtil.doGetAccessToken(code);
			String token=(String) doGetAccessToken.get("access_token");
			String openid=(String) doGetAccessToken.get("openid");
			String user_info = ReqURL.get_user_info;
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
			AuthResult authResult=JSONObject.parseObject(JSONObject.toJSONString(userInfo), AuthResult.class);
			model.addAttribute("authResult", authResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login_result";
	}
	
	@RequestMapping("sendTemplateMsg")
	@ResponseBody
	public String sendTemplateMsg(String code,Model model) {
		JSONObject send_template_result = null;
		try {
			WxTemplate template = new WxTemplate();
			template.setUrl("www.baidu.com");
			template.setTouser("o0BJQ5uR7L6QjrwRek8sklsuOXpc");
			template.setTopcolor("#000000");
			template.setTemplate_id("O7n71NjSoc2dB6w4XOEfRl4aGVOxo-nhVu7w8oJhxnE");
			Map<String,TemplateData> m = new HashMap<String,TemplateData>();
			TemplateData first = new TemplateData();
			first.setColor("#000000");
			first.setValue("您好，您有一条待确认订单。");
			m.put("first", first);
			TemplateData keyword1 = new TemplateData();
			keyword1.setColor("#328392");
			keyword1.setValue("OD0001");
			m.put("keyword1", keyword1);
			TemplateData keyword2 = new TemplateData();
			keyword2.setColor("#328392");
			keyword2.setValue("预定订单");
			m.put("keyword2", keyword2);
			TemplateData keyword3 = new TemplateData();
			keyword3.setColor("#328392");
			keyword3.setValue("大龙虾");
			m.put("keyword3", keyword3);
			TemplateData remark = new TemplateData();
			remark.setColor("#929232");
			remark.setValue("请及时确认订单！");
			m.put("remark", remark);
			template.setData(m);
			String template_access_token = authUtil.doAccessToken();
			String send_template_msg_url = ReqURL.send_template_msg;
			send_template_msg_url = send_template_msg_url.replaceAll("ACCESS_TOKEN", template_access_token);
			String dataSend=JSONObject.toJSONString(template);
			/**
			 * {
						"data": {
							"keyword3": {
								"color": "#328392",
								"value": "大龙虾"
							},
							"keyword1": {
								"color": "#328392",
								"value": "OD0001"
							},
							"keyword2": {
								"color": "#328392",
								"value": "预定订单"
							},
							"remark": {
								"color": "#929232",
								"value": "请及时确认订单！"
							},
							"first": {
								"color": "#000000",
								"value": "您好，您有一条待确认订单。"
							}
						},
						"template_id": "O7n71NjSoc2dB6w4XOEfRl4aGVOxo-nhVu7w8oJhxnE",
						"topcolor": "#000000",
						"touser": "o0BJQ5uR7L6QjrwRek8sklsuOXpc",
						"url": "www.baidu.com"
					}
			 */
			logger.info(dataSend);
			send_template_result = authUtil.doPOSTJson(send_template_msg_url, dataSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send_template_result.toJSONString();
	}
}
