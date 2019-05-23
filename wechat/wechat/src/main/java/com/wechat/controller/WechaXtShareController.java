package com.wechat.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.AppConfig;
import com.wechat.servcie.WeixinService;

/**
 * 微信分享
 * 
 * @author Administrator
 *
 */
@Controller
public class WechaXtShareController {
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private WeixinService weixinService;
	private final static Logger logger = LoggerFactory.getLogger(WechaXtShareController.class);

	@RequestMapping("cover")
	public String identifyCover(HttpServletRequest request, HttpServletResponse response) {
		// 微信分享授权开始
		String appId = appConfig.getAppID();// 取项目中配置的公众号id
		String secret = appConfig.getAppSecret();// 取项目中配置的公众号密钥
		// 例如我们有一个分享的链接为：http://test.weixinfwenx.cn/project/fenxiang.do?id=1&name=2;
		// 那么domainAddr = http://test.weixinfwenx.cn,这个可以动态的配置在项目里，方便测试环境和生产
		// 域名的切换
		String domainAddr = appConfig.getHost();// 项目中配置的网站的域名
		// 这个取的是链接上的参数，例如在上面的这个链接中，id=1&name=2就是我们要动态去的参数，可能有人
		// 会想到，这个两个参数直接写在地址中不是挺简单的为啥还要动态去获取这个参数呢；在这里我们引出了一
		// 个微信二次分享的问题，就是别人转发的链接给你，然后你再转发给别人，在你转发给别人后这个链接的签
		// 名就会失败，为啥呢，因为经过再次转发的链接，微信会自动加上一些自己的参数，这样会导致页面上微信
		// 分享的链接和签名的链接不一致。直接导致自定义的标题和链接描述，显示失败，失败原因是微信默认的在
		// 我们的分享链接上加上了&from=singlemessage。
		String str = request.getQueryString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", appId);
		map.put("secret", secret);
		String fenxurl = domainAddr + "/fenxiang.do?uuid="+UUID.randomUUID().toString();
		if(str!=null) {
			fenxurl = domainAddr + "/fenxiang.do?" + str+"&uuid="+UUID.randomUUID().toString();
		}
		
		logger.info("fenxurl=" + fenxurl);
		map.put("url", domainAddr + "/cover");
		// 这个地址是传给页面使用
		request.setAttribute("fenxurl", fenxurl);
		// 开始微信分享链接签名
		Map<String, String> params = weixinService.weixinjsIntefaceSign(map);
		params.put("domainAddr", domainAddr);
		request.setAttribute("params", params);
		logger.info("params=" + JSONObject.toJSONString(params));
		return "wx_share";
	}

	@RequestMapping("fenxiang.do")
	public String fenxiang(HttpServletRequest request, HttpServletResponse response) {
		// 微信分享授权开始
		String appId = appConfig.getAppID();// 取项目中配置的公众号id
		String secret = appConfig.getAppSecret();// 取项目中配置的公众号密钥
		// 例如我们有一个分享的链接为：http://test.weixinfwenx.cn/project/fenxiang.do?id=1&name=2;
		// 那么domainAddr = http://test.weixinfwenx.cn,这个可以动态的配置在项目里，方便测试环境和生产
		// 域名的切换
		String domainAddr = appConfig.getHost();// 项目中配置的网站的域名
		// 这个取的是链接上的参数，例如在上面的这个链接中，id=1&name=2就是我们要动态去的参数，可能有人
		// 会想到，这个两个参数直接写在地址中不是挺简单的为啥还要动态去获取这个参数呢；在这里我们引出了一
		// 个微信二次分享的问题，就是别人转发的链接给你，然后你再转发给别人，在你转发给别人后这个链接的签
		// 名就会失败，为啥呢，因为经过再次转发的链接，微信会自动加上一些自己的参数，这样会导致页面上微信
		// 分享的链接和签名的链接不一致。直接导致自定义的标题和链接描述，显示失败，失败原因是微信默认的在
		// 我们的分享链接上加上了&from=singlemessage。
		String str = request.getQueryString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", appId);
		map.put("secret", secret);
		String fenxurl = domainAddr + "/fenxiang.do";
		if(str!=null) {
			fenxurl = domainAddr + "/fenxiang.do?" + str;
		}
		logger.info("fenxurl=" + fenxurl);
		map.put("url",fenxurl);
		// 这个地址是传给页面使用
		request.setAttribute("fenxurl", fenxurl);
		// 开始微信分享链接签名
		Map<String, String> params = weixinService.weixinjsIntefaceSign(map);
		params.put("domainAddr", domainAddr);
		request.setAttribute("params", params);
		request.setAttribute("uuid", request.getParameter("uuid"));
		logger.info("params=" + JSONObject.toJSONString(params));
		return "fenxiang";
	}
}
