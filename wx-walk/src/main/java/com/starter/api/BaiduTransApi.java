package com.starter.api;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.config.BaiduApiConfiguration;
import com.starter.utils.MD5;

import lombok.extern.slf4j.Slf4j;

/**
 * 翻译
 *
 */
@Service
@Slf4j
public class BaiduTransApi {
	@Autowired
	private BaiduApiConfiguration baiduApiConfiguration;
	@Autowired
	private RestTemplate restTemplate;
	public String getTransResult(String content) throws UnsupportedEncodingException {
		log.info("传入的内容->" + content);
		String apiUrl = this.buildURL(content);
		String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
		log.info("翻译回复->" + result);
		JSONObject jsonResult = JSONObject.parseObject(result);
		JSONArray trans_result = jsonResult.getJSONArray("trans_result");
		if(trans_result!=null&&trans_result.size()>0) {
			return trans_result.getJSONObject(0).getString("dst");
		}
		return "无法翻译";
	}
	
	private String buildURL(String query) {
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());

        // 签名
        String src = baiduApiConfiguration.getTransAppid() + query + salt + baiduApiConfiguration.getTransSecurityKey(); // 加密前的原文
    
        String to = checkAllIsCN(query)?"en":"zh"; 
        return String.format(baiduApiConfiguration.getTransApiUrl(), query,to,
        		baiduApiConfiguration.getTransAppid(),
        		salt,MD5.md5(src));
    }
	
	public boolean checkAllIsCN(String name)
    {
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
}
