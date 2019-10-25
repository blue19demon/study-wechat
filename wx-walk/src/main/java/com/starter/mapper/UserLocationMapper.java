package com.starter.mapper;
import com.starter.base.mapper.MyMapper;
import com.starter.domain.UserLocation;

public interface UserLocationMapper extends MyMapper<UserLocation> {

	UserLocation getLastLocation(String openId);
}