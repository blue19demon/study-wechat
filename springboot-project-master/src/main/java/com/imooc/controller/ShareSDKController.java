package com.imooc.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.utils.Sha1;

import cn.hutool.json.JSONObject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 *
 * 微信分享
 *
 * @author pc
 *
 */
@Controller
public class ShareSDKController {

	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private StringRedisTemplate redisTemplate;

	private final static Logger logger = LoggerFactory.getLogger(ShareSDKController.class);

	/**
	 * 微信分享 第一步，生成分享配置签名参数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("wxConfig")
	@ResponseBody
	public Map<String, String> wxConfig(HttpServletRequest request, HttpServletResponse response) {
		String url=request.getParameter("url");
		// 开始微信分享链接签名
		Map<String, String> params =  weixinjsIntefaceSign(url);
		logger.info("params=" + new JSONObject(params).toStringPretty());
		return params;
	}

	/**
	 * 微信js-sdk分享
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, String> weixinjsIntefaceSign(String url) {
		// 查看缓存数据是否存在
		String cacheAccess_token = redisTemplate.opsForValue().get("access_token");
		String cacheTicket = redisTemplate.opsForValue().get("ticket");
		// 取出来为空的话则说明cacheAccess_token缓存过期，重新获取
		if (null == cacheAccess_token) {
			/////////////////////////////// start
			// 获取cacheAccess_token
			try {
				cacheAccess_token = wxMpService.getAccessToken();
			} catch (WxErrorException e) {
				e.printStackTrace();
			}
			redisTemplate.opsForValue().set("access_token", cacheAccess_token, 3600, TimeUnit.SECONDS);// 单位是秒
		}
		// 取出来为空的话则说明cacheTicket缓存过期，重新获取
		if (null == cacheTicket) {
			////////////////////////// start
			//// 获得jsapi_ticket
			try {
				cacheTicket = wxMpService.getJsapiTicket();
			} catch (WxErrorException e) {
				e.printStackTrace();
			}
			redisTemplate.opsForValue().set("ticket", cacheTicket, 3600, TimeUnit.SECONDS);// 单位是秒
		}
		
		// 生成签名
		SortedMap<Object, Object> params = new TreeMap<Object, Object>();
		params.put("timestamp", Long.toString(new Date().getTime() / 1000));
		params.put("noncestr", CreateNoncestr());
		params.put("jsapi_ticket", cacheTicket);
		params.put("url", url);// url地址
		StringBuffer sb = new StringBuffer();
		Set<?> es = params.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		String signStr = sb.toString().substring(0, sb.toString().length() - 1);
		String sign = Sha1.getSha1Sign(signStr);// 签名
		Map<String, String> result = new HashMap<String, String>();
		result.put("timestamp", (String) params.get("timestamp"));
		result.put("noncestr", (String) params.get("noncestr"));
		result.put("signature", sign);
		result.put("appId", wxMpService.getWxMpConfigStorage().getAppId());
		logger.info("cacheTicket=" + cacheTicket);
		return result;
	}

	private String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

}
