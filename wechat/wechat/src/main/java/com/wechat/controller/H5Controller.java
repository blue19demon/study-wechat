package com.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * H5 api
 * 
 * @author pc
 *
 */
@Controller
public class H5Controller {

	@RequestMapping("h5model")
    public String h5model(HttpServletRequest request) {
		request.setAttribute("phone", getPhoneDevice(request));
        return "h5model";
    }
	
	private Map<String, String> getPhoneDevice(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String referrer = request.getHeader("Referer");
        System.out.println(referrer);
        String remoteAddr = getIpAddr(request);
        map.put("remoteAddr", remoteAddr);
        String requestHeader = request.getHeader("User-Agent");
        System.out.println("requestHeader：" + requestHeader);
        int index_one = requestHeader.indexOf("(");
        String requestBody = requestHeader.substring(index_one+1);
        String userInfo = requestBody.substring(0, requestBody.indexOf(")"));
        String[] userInfoList = userInfo.split(";");
        int length = userInfoList.length;
        String os = userInfoList[0];
        String mobileInfo = userInfoList[length - 1];
        if(os.equals("Windows NT 6.1")){
            System.out.println("您的操作系统为：windows7");
            map.put("os", "windows7");
        }else{
            System.out.println("您的操作系统为：" + os);
            map.put("os", os);
        }
        System.out.println("mobileInfo:"+mobileInfo);
        int index = mobileInfo.indexOf("/");
        map.put("mobileInfo", "未知");
        if(index > 0){
            mobileInfo = mobileInfo.substring(0, mobileInfo.indexOf("/") - 5);
            System.out.println("您的手机型号为：" + mobileInfo);
            map.put("mobileInfo", mobileInfo);
        }
		return map;
	}
	
	/** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值 
     *  
     * @return ip
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for"); 
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {  
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
            System.out.println("Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
            System.out.println("X-Real-IP ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
            System.out.println("getRemoteAddr ip: " + ip);
        } 
        System.out.println("获取客户端ip: " + ip);
        return ip;  
    }
}
