package com.wechat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
			access_token_url = access_token_url.replaceAll("APPID", "wx90afd5a95cf62c57").replaceAll("APPSECRET",
					"73071d926b9174529eb433010f1d3586");
			JSONObject jsonObject = doGetJson(access_token_url);
			System.out.println(jsonObject.toJSONString());
			template_access_token = jsonObject.getString("access_token");
			logger.info("template_access_token=" + template_access_token);
			RedisUtils.write("template_access_token", template_access_token, 7200);
			return template_access_token;
		}
	}
	
	/**
	 * 上传永久素材
	 * @param	file
	 * @param	type
	 * @param	title type为video时需要,其他类型设null
	 * @param	introduction type为video时需要,其他类型设null
	 * @return	{"media_id":MEDIA_ID,"url":URL}
	 */
	public  String uploadPermanentMaterial(InputStream input, String type, String title, String introduction, String ext) {
		
		String access_token = null;
		try {
			access_token = new AuthUtil().doAccessToken();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token="
				+ access_token + "&type=" + type;
 
		String result = null;
 
		try {
			URL uploadURL = new URL(url);
 
			HttpURLConnection conn = (HttpURLConnection) uploadURL.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Cache-Control", "no-cache");
			String boundary = "-----------------------------" + System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
 
			OutputStream output = conn.getOutputStream();
			output.write(("--" + boundary + "\r\n").getBytes());
			output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n",UUID.randomUUID().toString().replaceAll("-", "")+ext).getBytes());
			output.write("Content-Type: video/mp4 \r\n\r\n".getBytes());
 
			byte[] data = new byte[1024];
			int len = 0;
			/* FileInputStream input = new FileInputStream(file); */
			while ((len = input.read(data)) > -1) {
				output.write(data, 0, len);
			}
 
			/*对类型为video的素材进行特殊处理*/
			if ("video".equals(type)) {
				output.write(("--" + boundary + "\r\n").getBytes());
				output.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
				output.write(String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}", title, introduction).getBytes());
			}
 
			output.write(("\r\n--" + boundary + "--\r\n\r\n").getBytes());
			output.flush();
			output.close();
			input.close();
			
			InputStream resp = conn.getInputStream();
 
			StringBuffer sb = new StringBuffer();
 
			while ((len = resp.read(data)) > -1)
				sb.append(new String(data, 0, len, "utf-8"));
			resp.close();
			result = sb.toString();
		} catch (IOException e) {
			//....
		}
		
		return result;
	}
	
	/**
     * 获取网络图片流
     * 
     * @param url
     * @return
     */
    public InputStream getNetStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }
}
