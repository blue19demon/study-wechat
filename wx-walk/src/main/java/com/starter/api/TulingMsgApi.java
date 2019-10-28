package com.starter.api;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.starter.config.AppConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 调用图灵机器人api接口，获取智能回复内容
 */
@Service
@Slf4j
public class TulingMsgApi {
	/**
	 * 调用图灵机器人api接口，获取智能回复内容，解析获取自己所需结果
	 * 
	 * @param content
	 * @return
	 */
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private AppConfiguration appConfiguration;
	public String getTulingResult(String content) throws UnsupportedEncodingException {
		log.info("传入的内容->" + content);
		String apiUrl = String.format(appConfiguration.getTulingAPI(), appConfiguration.getTulingKey(),URLEncoder.encode(content, "utf-8"));
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info("机器人回复->" + result);
		/** 请求失败处理 */
		if (null == result) {
			return "对不起，你说的话真是太高深了……";
		}
		try {
			StringBuffer bf = new StringBuffer();
			String s = "";
			JSONObject json = JSONObject.parseObject(result);
			/**
			 * code 说明 100000文本类 200000链接类 302000新闻类 308000菜谱类
			 */
			if (100000 == json.getInteger("code")) {
				s = json.getString("text");
				bf.append(s);
			} else if (200000 == json.getInteger("code")) {
				s = json.getString("text");
				bf.append(s);
				bf.append("\n");
				s = json.getString("url");
				bf.append(s);
			} else if (302000 == json.getInteger("code")) {
				s = "待开发有点麻烦！\n";
				bf.append(s);
			} else if (308000 == json.getInteger("code")) {
				s = "待开发有点麻烦！\n";
				bf.append(s);
			}
			result = bf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if("".equals(result)) {
			result="无法回答你的问题";
		}
		return result;
	}
}