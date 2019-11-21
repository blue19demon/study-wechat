package com.starter.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.config.app.AppConfiguration;
import com.starter.config.app.GaodeMapConfiguration;
import com.starter.pojo.GaodePlace;

/**
 * 高德地图操作类
 */
@Service
public class GaodeMapApi {
	@Autowired
	private GaodeMapConfiguration gaodeMapConfiguration;
	@Autowired
	private AppConfiguration appConfiguration;
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 圆形区域检索
	 * 
	 * @param query 检索关键词
	 * @param lng   经度
	 * @param lat   纬度
	 * @return List<BaiduPlace>
	 * @throws UnsupportedEncodingException
	 */
	public List<GaodePlace> searchPlace(String query, String lng, String lat) throws Exception {
		// 拼装请求地址
		String api = String.format(gaodeMapConfiguration.getAroundSearchUrl(), gaodeMapConfiguration.getWebkey(),
				lng + "," + lat, query, appConfiguration.getRadius());
		// 调用Place API圆形区域检索
		String respJSON = httpRequest(api);
		// 解析返回的xml
		List<GaodePlace> placeList = parsePlaceJSON(respJSON);
		return placeList;
	}

	private List<GaodePlace> parsePlaceJSON(String respJSON) {
		JSONObject jsonObject = JSONObject.parseObject(respJSON);
		if (jsonObject.getString("status").equals("1")) {
			Integer count = Integer.parseInt(jsonObject.getString("count"));
			if (count > 0) {
				List<GaodePlace> placeList = new ArrayList<GaodePlace>();
				JSONArray pois = jsonObject.getJSONArray("pois");
				for (int i = 0; i < pois.size(); i++) {
					JSONObject poi = pois.getJSONObject(i);
					placeList.add(new GaodePlace(poi.getString("name"), poi.getString("address"),
							poi.getString("location"), poi.getString("tel"), Integer.parseInt(poi.getString("distance"))));
				}
				// 按距离由近及远排序
				Collections.sort(placeList);
				return placeList;
			}
		}
		return null;
	}

	/**
	 * 发送http请求
	 * 
	 * @param requestUrl 请求地址
	 * @return String
	 */
	public String httpRequest(String requestUrl) {
		return restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class).getBody();
	}

	/**
	 * 根据Place组装图文列表
	 * 
	 * @param placeList
	 * @param bd09Lng   经度
	 * @param bd09Lat   纬度
	 * @return List<Article>
	 */
	public String makeArticleList(List<GaodePlace> placeList, String lng, String lat) {
		// 项目的根路径
		String basePath = appConfiguration.getServerUrl();
		GaodePlace place = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append("高德地图：\n");
		int count = placeList.size();
		int real= 0;
		for (int i = 0; i < count; i++) {
			place = placeList.get(i);
			String location=place.getLocation();
			if(!StringUtils.isEmpty(location)) {
				String[] locations = location.split(",");
				if(locations!=null&&locations.length==2) {
					buffer.append(place.getName() + "[地址："+place.getAddress()+";距离："+place.getDistance()+"米]\n");
					String route = String.format(basePath + "/gaode/routeWalk?p1Lng=%s&p1Lan=%s&p2Lng=%s&p2Lan=%s", lng, lat,locations[0],locations[1] );
					buffer.append("<a href='" + route + "'>步行导航</a>").append("\n");
					
					String routeBus = String.format(basePath + "/gaode/routeBus?p1Lng=%s&p1Lan=%s&p2Lng=%s&p2Lan=%s", lng, lat,locations[0],locations[1] );
					buffer.append("<a href='" + routeBus + "'>公交导航</a>").append("\n");
					real++;
					if(real>=5) {
						break;
					}
				}
			}
			
		}
		return buffer.toString();
	}
}
