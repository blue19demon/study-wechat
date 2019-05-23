package com.wechat.share;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.AppConfig;

/**
 * 
 * 微信分享
 * @author pc
 *
 */
@Controller
public class ShareController {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private WechatAuthService wechatAuthService;
    private final static Logger logger = LoggerFactory.getLogger(ShareController.class);

    @RequestMapping("wxConfig")
    @ResponseBody
    public Map<String, String> wxConfig(HttpServletRequest request, HttpServletResponse response) {
        // 微信分享授权开始
        String appId = appConfig.getAppID();// 取项目中配置的公众号id
        String secret = appConfig.getAppSecret();
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", appId);
        map.put("secret", secret);
        map.put("url", request.getParameter("url"));
        // 开始微信分享链接签名
        Map<String, String> params = wechatAuthService.weixinjsIntefaceSign(map);
        logger.info("params=" + JSONObject.toJSONString(params));
        return params;
    }
    
    @RequestMapping("wxTrigger")
    @ResponseBody
    public Map<String, String> wxTrigger(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", request.getParameter("url"));
        logger.info("wxTrigger=" + JSONObject.toJSONString(map));
        return map;
    }
    
    @RequestMapping("wxShreTest")
    public String wxShreTest(HttpServletRequest request) {
    	String domainAddr = appConfig.getHost();
    	request.setAttribute("domainAddr", domainAddr);
    	request.setAttribute("id", UUID.randomUUID().toString());
        return "test_share";
    }
    
    @RequestMapping("wxShreTest2")
    public String wxShreTest2(HttpServletRequest request) {
    	String domainAddr = appConfig.getHost();
    	request.setAttribute("domainAddr", domainAddr);
    	request.setAttribute("id", request.getParameter("id"));
        return "test_share2";
    }
    
    @RequestMapping("wxShreTest3")
    public String wxShreTest3(HttpServletRequest request) {
    	String domainAddr = appConfig.getHost();
    	request.setAttribute("domainAddr", domainAddr);
    	request.setAttribute("id", request.getParameter("id"));
        return "test_share3";
    }
}
