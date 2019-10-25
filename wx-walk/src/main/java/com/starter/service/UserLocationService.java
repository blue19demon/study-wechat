package com.starter.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starter.domain.UserLocation;
import com.starter.mapper.UserLocationMapper;

@Service
public class UserLocationService {
	@Autowired
    private UserLocationMapper userLocationMapper ;
	public List<UserLocation> getList() {
		return userLocationMapper.selectAll();
	}
	
	public int save(UserLocation record) {
		return userLocationMapper.insertSelective(record);
	}

	public UserLocation getLastLocation(HttpServletRequest request, String openId) {
		return userLocationMapper.getLastLocation(openId);
	}

	public void saveUserLocation(String locationCN, String openId, String lng, String lat,
			String bd09Lng, String bd09Lat) {
		save(UserLocation.builder().openId(openId).lat(lat).lng(lng).bd09Lat(bd09Lat).bd09Lng(bd09Lng).locationCN(locationCN).created(new Date()).build());
	}
}
