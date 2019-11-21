package com.starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starter.api.enums.MapProvider;

import redis.clients.jedis.JedisPool;

@Service
public class JedisService {
	@Autowired
	private JedisPool jedisPool;
	private static final String MAP_PROVIDER = "map_provider_";
	private static final String MAP_SEARCH = "map_search_";

	public void setSearch(String openId, String search) {
		jedisPool.getResource().set(MAP_SEARCH.concat(openId), search);
	}

	public String getSearch(String openId) {
		return jedisPool.getResource().get(MAP_SEARCH.concat(openId));
	}

	public void setProvider(String openId, MapProvider provider) {
		jedisPool.getResource().set(MAP_PROVIDER.concat(openId), provider.name());
	}

	public String getProvider(String openId) {
		return jedisPool.getResource().get(MAP_PROVIDER.concat(openId));
	}

	public void set(String key, String value) {
		jedisPool.getResource().set(key, value);
	}

	public String get(String key) {
		return jedisPool.getResource().get(key);
	}
}
