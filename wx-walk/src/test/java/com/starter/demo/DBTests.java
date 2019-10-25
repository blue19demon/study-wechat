package com.starter.demo;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.starter.domain.UserLocation;
import com.starter.service.UserLocationService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DBTests {
	@Autowired
	private UserLocationService userLocationService;

	@Test
	public void getList() {
		log.info(JSONObject.toJSONString(userLocationService.getList(), true));
	}
	
	@Test
	public void save() {
		userLocationService.save(UserLocation.builder().openId("123").lat("a").lng("2").bd09Lat("1").bd09Lng("2").created(new Date()).build());
	}
}
