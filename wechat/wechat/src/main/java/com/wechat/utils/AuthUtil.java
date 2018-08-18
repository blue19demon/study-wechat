package com.wechat.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
        JSONObject jsonObject=null;
        DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);
        HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
        HttpEntity httpEntity=httpResponse.getEntity();
        if(httpEntity!=null){
            String result= EntityUtils.toString(httpEntity,"UTF-8");
            jsonObject=JSONObject.parseObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
    
    @SuppressWarnings({ })
	public Map<String,Object> doGetAccessToken(String code) throws IOException {
        String access_token=RedisUtils.read("access_token");
        String openid=RedisUtils.read("openid");
        Map<String,Object> map=new HashMap<>();
        if(access_token!=null&&!"".equals(access_token)) {
        	logger.info("从缓存中获取！");
        	map.put("access_token", access_token);
        	map.put("openid", openid);
        	return map;
        }else {
        	String access_token_url  = ReqURL.get_access_token;
        	access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID())
					.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
			JSONObject jsonObject = doGetJson(access_token_url);
			openid = jsonObject.getString("openid");
			access_token = jsonObject.getString("access_token");
			logger.info("openid="+openid);
			logger.info("access_token="+access_token);
			map.put("access_token", access_token);
        	map.put("openid", openid);
        	RedisUtils.write("access_token", access_token, 7200);
        	RedisUtils.write("openid", openid, 7200);
        	return map;
        }
		
    }
}
