package com.wechat.wechat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) {

		try {
			// 获取sandbox_signkey
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", "1512382871");// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("sign", WXPayUtil.generateSignature(param1, "Qixingbaoduoduozhuanqian85910855",
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", xml);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			System.out.println(JSONObject.toJSONString(res));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String doPOSTJson(String url, String data) throws IOException {
		String result = null;

		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();

		StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			result = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return result;
	}
}
