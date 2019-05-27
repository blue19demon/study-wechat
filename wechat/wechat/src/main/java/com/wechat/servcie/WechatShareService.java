package com.wechat.servcie;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.utils.RedisUtils;
import com.wechat.utils.Sha1;

import redis.clients.jedis.Jedis;
@Service
public class WechatShareService {
    private final static Logger logger = LoggerFactory.getLogger(WechatShareService.class);

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

	    public JSONObject doPOSTJson(String url, String data) throws IOException {
	        JSONObject jsonObject = null;
	        HttpPost httpPost = new HttpPost(url);
	        CloseableHttpClient client = HttpClients.createDefault();

	        StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
	        entity.setContentEncoding("UTF-8");
	        entity.setContentType("application/json");
	        httpPost.setEntity(entity);
	        HttpResponse resp = client.execute(httpPost);
	        if (resp.getStatusLine().getStatusCode() == 200) {
	            HttpEntity httpEntity = resp.getEntity();
	            String result = EntityUtils.toString(httpEntity, "UTF-8");
	            jsonObject = JSONObject.parseObject(result);
	        }
	        return jsonObject;
	    }

	    /**
	     * 微信js-sdk分享
	     * @param map
	     * @return
	     */
	    @SuppressWarnings("rawtypes")
	    public Map<String, String> weixinjsIntefaceSign(Map<String, String> map) {
	        // 查看缓存数据是否存在
	        String cacheAccess_token = RedisUtils.read("access_token");
	        String cacheTicket = RedisUtils.read("ticket");
	        Jedis jedis = RedisUtils.getJedis();
	        // 取出来为空的话则说明cacheAccess_token缓存过期，重新获取
	        if (null == cacheAccess_token) {
	            /////////////////////////////// start
	            // 获取cacheAccess_token
	            StringBuffer buffer = new StringBuffer();
	            buffer.append("https://api.weixin.qq.com/cgi-bin/token?");
	            buffer.append("appid=" + map.get("appid"));
	            buffer.append("&secret=" + map.get("secret"));
	            buffer.append("&grant_type=client_credential");
	            JSONObject json = null;
	            try {
	                json = this.doGetJson(buffer.toString());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            ///////////////////// end
	            cacheAccess_token = json.getString("access_token");
	        }
	        // 取出来为空的话则说明cacheTicket缓存过期，重新获取
	        if (null == cacheTicket) {
	            ////////////////////////// start
	            //// 获得jsapi_ticket
	            StringBuffer buffer = new StringBuffer();
	            buffer.append("https://api.weixin.qq.com/cgi-bin/ticket/getticket?");
	            buffer.append("access_token=" + cacheAccess_token);
	            buffer.append("&type=jsapi");
	            ///////////////////// end

	            JSONObject json2 = null;
	            try {
	                json2 = doGetJson(buffer.toString());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            cacheTicket = json2.getString("ticket");
	            jedis.set("ticket", cacheTicket, "NX", "EX", 3600);// 单位是秒
	        }
	        // 生成签名
	        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
	        params.put("timestamp", Long.toString(new Date().getTime() / 1000));
	        params.put("noncestr", CreateNoncestr());
	        params.put("jsapi_ticket", cacheTicket);
	        params.put("url", map.get("url"));// url地址
	        StringBuffer sb = new StringBuffer();
	        Set<?> es = params.entrySet();
	        Iterator<?> it = es.iterator();
	        while (it.hasNext()) {
	            Map.Entry entry = (Map.Entry) it.next();
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
	        result.put("appId", map.get("appid"));
	        logger.info("cacheTicket=" + cacheTicket);
	        return result;
	    }
	    
	    private  String CreateNoncestr() {
			String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			String res = "";
			for (int i = 0; i < 16; i++) {
				Random rd = new Random();
				res += chars.charAt(rd.nextInt(chars.length() - 1));
			}
			return res;
	      }
}
