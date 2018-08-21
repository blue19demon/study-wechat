package com.wechat.utils;

import redis.clients.jedis.Jedis;

public class RedisUtils {
	static Jedis jedis =null;
	static {
		 //连接本地的 Redis 服务
        jedis = new Jedis("localhost");
	}
	
	public static String read(String key) {
		try {
			return  jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void write(String key,String value,int expTime) {
		try {
			jedis.set(key, value);
			jedis.expire(key, expTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}