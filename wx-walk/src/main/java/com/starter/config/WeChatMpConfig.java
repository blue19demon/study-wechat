package com.starter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.starter.config.disconf.RedisConfiguration;
import com.starter.config.disconf.WxMpConfiguration;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import redis.clients.jedis.JedisPool;
 
@Configuration
@Slf4j
public class WeChatMpConfig {
 
    @Autowired
    private WxMpConfiguration wxMpConfiguration;
    @Autowired
    private RedisConfiguration redisConfiguration;
 
    @Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }
 
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpRedisConfigImpl wxMpConfigStorage = new WxMpRedisConfigImpl(jedisPool());
        wxMpConfigStorage.setAppId(wxMpConfiguration.getAppId());
        wxMpConfigStorage.setSecret(wxMpConfiguration.getSecret());
        return wxMpConfigStorage;
    }
    
    @Bean
    public JedisPool jedisPool() {
    	log.info("redisConfiguration="+JSONObject.toJSONString(redisConfiguration, true));
        JedisPool pool = new JedisPool(redisConfiguration.getHost(), Integer.parseInt(redisConfiguration.getPort()));
        return pool;
    }
}