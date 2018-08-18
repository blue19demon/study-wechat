package com.wechat.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.AppConfig;
import com.wechat.config.ReqURL;

@SuppressWarnings("deprecation")
@Configuration
public class AuthUtil {
	private final static Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	@Autowired
	private AppConfig appConfig;

	@SuppressWarnings({ "resource" })
	public JSONObject doGetJson(String url) throws IOException {
		JSONObject jsonObject = null;
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		httpGet.releaseConnection();
		return jsonObject;
	}

	@SuppressWarnings({ "resource" })
	public JSONObject doPOSTJson(String url, String data) throws IOException {
		JSONObject jsonObject = null;

		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		
		StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		System.out.println();

//        表单方式
//        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
//        pairList.add(new BasicNameValuePair("name", "admin"));
//        pairList.add(new BasicNameValuePair("pass", "123456"));
//        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));   

		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}

	@SuppressWarnings({})
	public Map<String, Object> doGetAccessToken(String code) throws IOException {
		String access_token = RedisUtils.read("access_token");
		String openid = RedisUtils.read("openid");
		Map<String, Object> map = new HashMap<>();
		if (access_token != null && !"".equals(access_token)) {
			logger.info("从缓存中获取！");
			map.put("access_token", access_token);
			map.put("openid", openid);
			return map;
		} else {
			String access_token_url = ReqURL.get_access_token;
			access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID())
					.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
			JSONObject jsonObject = doGetJson(access_token_url);
			openid = jsonObject.getString("openid");
			access_token = jsonObject.getString("access_token");
			logger.info("openid=" + openid);
			logger.info("access_token=" + access_token);
			map.put("access_token", access_token);
			map.put("openid", openid);
			RedisUtils.write("access_token", access_token, 7200);
			RedisUtils.write("openid", openid, 7200);
			return map;
		}
	}

	@SuppressWarnings({})
	public String doAccessToken() throws IOException {
		String template_access_token = RedisUtils.read("template_access_token");

		if (template_access_token != null && !"".equals(template_access_token)) {
			logger.info("从缓存中获取！");
			return template_access_token;
		} else {
			String access_token_url = ReqURL.access_token_url;
			access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID()).replaceAll("APPSECRET",
					appConfig.getAppSecret());
			JSONObject jsonObject = doGetJson(access_token_url);
			template_access_token = jsonObject.getString("access_token");
			logger.info("template_access_token=" + template_access_token);
			RedisUtils.write("template_access_token", template_access_token, 7200);
			return template_access_token;
		}
	}
}
