package com.starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.starter.config.app.AppConfiguration;
import com.starter.framework.RestResultDto;

@Service
public class RemoteCommandService {

	@Autowired
	private AppConfiguration appConfiguration;
	@Autowired
	private RestTemplate restTemplate;

	public String excuteStart() {
		RestResultDto restResultDto = restTemplate.exchange(appConfiguration.getRemoteStart(), HttpMethod.GET, null, RestResultDto.class).getBody();
		if(restResultDto.isSuccess()) {
			return (String) restResultDto.getBody();
		}
		return restResultDto.getMessage();
	}

	public String excuteStop() {
		RestResultDto restResultDto = restTemplate.exchange(appConfiguration.getRemoteStop(), HttpMethod.GET, null, RestResultDto.class)
				.getBody();
		if(restResultDto.isSuccess()) {
			return (String) restResultDto.getBody();
		}
		return restResultDto.getMessage();
	}

}