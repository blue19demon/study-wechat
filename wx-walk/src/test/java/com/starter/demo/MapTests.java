package com.starter.demo;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.starter.config.BaiduMapConfiguration;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MapTests {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BaiduMapConfiguration baiduMapConfiguration;
	@Test
	public void geocoder() throws UnsupportedEncodingException {
		String api = String.format(baiduMapConfiguration.getUrlGetLocation(), "39.912176,116.683371", baiduMapConfiguration.getAk());
		ResponseEntity<String> obj = restTemplate.exchange(api.toString(), HttpMethod.GET, null, String.class);
		log.info(new String(obj.getBody().getBytes("UTF8")));
	}
	
	@Test
	public void placeSearch() {
		String api = String.format(baiduMapConfiguration.getUrlPlaceSearch(),"银行","30.586656290951,104.06857289693", baiduMapConfiguration.getAk());
		ResponseEntity<String> obj = restTemplate.exchange(api.toString(), HttpMethod.GET, null, String.class);
		log.info(obj.getBody());
	}
}
