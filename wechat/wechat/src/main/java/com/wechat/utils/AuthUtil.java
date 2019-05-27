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
import com.wechat.config.WechatAPIURL;
import com.wechat.config.WechatParamConfig;

@SuppressWarnings("deprecation")
@Configuration
public class AuthUtil {
	private final static Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	@Autowired
	private WechatParamConfig appConfig;

	@SuppressWarnings({ "resource" })
	public JSONObject doGetJson(String url) throws IOException {
		logger.info("从网络获取>>>>>>>" + url);
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

	public Map<String, Object> doGetAccessToken(String code) throws IOException {
		String access_token_from_redis = RedisUtils.read("access_token");
		Map<String, Object> map = new HashMap<>();
		JSONObject jsonObject = null;
		if (access_token_from_redis != null && !"".equals(access_token_from_redis)) {
			logger.info("从缓存中获取access_token:" + access_token_from_redis);
			map.put("access_token", access_token_from_redis);
			String open_id_from_redis = RedisUtils.read(access_token_from_redis);
			if (open_id_from_redis != null && !"".equals(open_id_from_redis)) {
				logger.info("从缓存中获取open_id:" + open_id_from_redis);
				map.put("expires_in", 7200);
				map.put("openid", open_id_from_redis);
				return map;
			} else {
				// 缓存找不到openId
				String access_token_url = WechatAPIURL.get_access_token;
				access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID())
						.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
				jsonObject = doGetJson(access_token_url);
				String openid = jsonObject.getString("openid");
				String access_token = jsonObject.getString("access_token");
				String refresh_token = jsonObject.getString("refresh_token");
				String expires_in = jsonObject.getString("expires_in");
				/**
				 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
				 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
				 */
				logger.info(jsonObject.toJSONString());
				RedisUtils.write("access_token", access_token, Integer.parseInt(expires_in) - 10);
				RedisUtils.write(access_token, openid, Integer.parseInt(expires_in) - 10);
				RedisUtils.write("refresh_token", refresh_token, Integer.parseInt(expires_in) - 10);
				map.put("openid", open_id_from_redis);
				map.put("expires_in", expires_in);
				return map;
			}
		} else {
			// 缓存找不到access_token
			String refresh_token = RedisUtils.read("refresh_token");
			if (refresh_token != null && !"".equals(refresh_token)) {
				String refresh_token_url = WechatAPIURL.get_refresh_token;
				refresh_token_url = refresh_token_url.replaceAll("APPID", appConfig.getAppID())
						.replaceAll("REFRESH_TOKEN", refresh_token);
				jsonObject = doGetJson(refresh_token_url);
				logger.info("刷新获取！");
				String access_token = jsonObject.getString("access_token");
				String expires_in = jsonObject.getString("expires_in");
				/**
				 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
				 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
				 */
				RedisUtils.write("access_token", access_token, Integer.parseInt(expires_in) - 10);
				logger.info("refresh_token>>>>>>>>>" + jsonObject.toJSONString());
				map.put("access_token", access_token);

				if (access_token_from_redis != null && !"".equals(access_token_from_redis)) {
					String open_id_from_redis = RedisUtils.read(access_token_from_redis);
					logger.info("从缓存中获取openid:" + open_id_from_redis);
					map.put("openid", open_id_from_redis);
					map.put("expires_in", expires_in);
					return map;
				} else {
					// 缓存找不到openId
					String access_token_url = WechatAPIURL.get_access_token;
					access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID())
							.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
					jsonObject = doGetJson(access_token_url);
					String openid = jsonObject.getString("openid");
					access_token = jsonObject.getString("access_token");
					refresh_token = jsonObject.getString("refresh_token");
					expires_in = jsonObject.getString("expires_in");
					/**
					 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
					 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
					 */
					logger.info(jsonObject.toJSONString());
					RedisUtils.write("access_token", access_token, Integer.parseInt(expires_in) - 10);
					RedisUtils.write(access_token, openid, Integer.parseInt(expires_in) - 10);
					RedisUtils.write("refresh_token", refresh_token, Integer.parseInt(expires_in) - 10);
					map.put("openid", openid);
					map.put("expires_in", expires_in);
					return map;
				}
			} else {
				// 缓存找不到openId
				String access_token_url = WechatAPIURL.get_access_token;
				access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID())
						.replaceAll("SECRET", appConfig.getAppSecret()).replaceAll("CODE", code);
				jsonObject = doGetJson(access_token_url);
				String openid = jsonObject.getString("openid");
				String access_token = jsonObject.getString("access_token");
				refresh_token = jsonObject.getString("refresh_token");
				String expires_in = jsonObject.getString("expires_in");
				/**
				 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
				 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
				 */
				logger.info(jsonObject.toJSONString());
				RedisUtils.write("access_token", access_token, Integer.parseInt(expires_in) - 10);
				RedisUtils.write(access_token, openid, Integer.parseInt(expires_in) - 10);
				RedisUtils.write("refresh_token", refresh_token, Integer.parseInt(expires_in) - 10);
				map.put("access_token", access_token);
				map.put("openid", openid);
				map.put("expires_in", expires_in);
				return map;
			}
		}
	}

	@SuppressWarnings({})
	public String doAccessToken() throws IOException {
		String template_access_token = RedisUtils.read("template_access_token");

		if (template_access_token != null && !"".equals(template_access_token)) {
			logger.info("从缓存中获取！");
			return template_access_token;
		} else {
			String access_token_url = WechatAPIURL.access_token_url;
			access_token_url = access_token_url.replaceAll("APPID", appConfig.getAppID()).replaceAll("APPSECRET",
					appConfig.getAppSecret());
			JSONObject jsonObject = doGetJson(access_token_url);
			logger.info(jsonObject.toJSONString());
			template_access_token = jsonObject.getString("access_token");
			logger.info("template_access_token=" + template_access_token);
			RedisUtils.write("template_access_token", template_access_token, 7200);
			return template_access_token;
		}
	}

	/**
	 * 上传永久素材
	 * 
	 * @param file
	 * @param type
	 * @param title        type为video时需要,其他类型设null
	 * @param introduction type为video时需要,其他类型设null
	 * @return {"media_id":MEDIA_ID,"url":URL}
	 */
	public String uploadPermanentMaterial(InputStream input, String type, String title, String introduction,
			String ext) {

		String access_token = null;
		try {
			access_token = new AuthUtil().doAccessToken();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + access_token + "&type="
				+ type;

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
			output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n",
					UUID.randomUUID().toString().replaceAll("-", "") + ext).getBytes());
			output.write("Content-Type: video/mp4 \r\n\r\n".getBytes());

			byte[] data = new byte[1024];
			int len = 0;
			/* FileInputStream input = new FileInputStream(file); */
			while ((len = input.read(data)) > -1) {
				output.write(data, 0, len);
			}

			/* 对类型为video的素材进行特殊处理 */
			if ("video".equals(type)) {
				output.write(("--" + boundary + "\r\n").getBytes());
				output.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
				output.write(
						String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}", title, introduction).getBytes());
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
			// ....
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
