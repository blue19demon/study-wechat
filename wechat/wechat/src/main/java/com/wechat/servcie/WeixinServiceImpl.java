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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.utils.AuthUtil;
import com.wechat.utils.Sha1;

import redis.clients.jedis.Jedis;

@Service
public class WeixinServiceImpl implements WeixinService {
	static Jedis jedis =null;
	@Autowired
	private AuthUtil authUtil;
	static {
		 //连接本地的 Redis 服务
        jedis = new Jedis("localhost");
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String> weixinjsIntefaceSign(Map<String, String> map) {
		 //查看缓存数据是否存在
		  String cacheAccess_token = jedis.get("access_token");
		  String cacheTicket = jedis.get("ticket");
		  //取出来为空的话则说明cacheAccess_token缓存过期，重新获取
		  if(null == cacheAccess_token){
			  ///////////////////////////////start
			  //获取cacheAccess_token   
			  //这段代码实际开发过程中要写成一个方法，我这里为了演示方便写在了一起。
			  StringBuffer buffer = new StringBuffer();
			  buffer.append("https://api.weixin.qq.com/cgi-bin/token?");
			  buffer.append("appid="+map.get("appid"));
			  buffer.append("&secret="+map.get("secret"));
			  buffer.append("&grant_type=client_credential");
			  JSONObject json = null;
			try {
				json = authUtil.doGetJson(buffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			  ///////////////////// end
			  cacheAccess_token = json.getString("access_token");
			  jedis.set("access_token",cacheAccess_token, "NX", "EX", 3600);//单位是秒
		  }
		  //取出来为空的话则说明cacheTicket缓存过期，重新获取
		  if(null == cacheTicket){
			  ////////////////////////// start
			  ////获得jsapi_ticket
			  StringBuffer buffer = new StringBuffer();
			  buffer.append("https://api.weixin.qq.com/cgi-bin/ticket/getticket?");
			  buffer.append("access_token="+cacheAccess_token);
			  buffer.append("&type=jsapi");
			  ///////////////////// end
			  
			  JSONObject json2 = null;
			try {
				json2 = authUtil.doGetJson(buffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			  cacheTicket = json2.getString("ticket");
			  jedis.set("ticket",cacheTicket, "NX", "EX", 3600);//单位是秒	
		  }
		  //生成签名
		  SortedMap<Object,Object> params = new TreeMap<Object,Object>();	       
		  params.put("timestamp", Long.toString(new Date().getTime()/1000));
		  params.put("noncestr", this.CreateNoncestr());
		  params.put("jsapi_ticket",cacheTicket);
		  params.put("url",map.get("url"));//url地址
		  StringBuffer sb = new StringBuffer();
		  Set<?> es = params.entrySet();
		  Iterator<?> it = es.iterator();
		  while(it.hasNext()) {
			  Map.Entry entry = (Map.Entry)it.next();
			  String k = (String)entry.getKey();
			  Object v = entry.getValue();
			  sb.append(k + "=" + v + "&");
		  }
		  String  signStr = sb.toString().substring(0, sb.toString().length()-1);
		  String sign = Sha1.getSha1Sign(signStr);//签名	
		  Map<String, String> result = new HashMap<String,String>();
		  result.put("timestamp",(String)params.get("timestamp"));
		  result.put("noncestr", (String)params.get("noncestr"));
		  result.put("signature", sign);
		  result.put("appId",map.get("appid"));
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
