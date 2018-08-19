package com.wechat.utils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom2.Document;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.wechat.bean.AuthToken;
import com.wechat.bean.WxPaySendData;
import com.wechat.config.WeChatConfig;
import com.wechat.config.WeChatConstant;
public class WeChatUtils {
	 
    /**
     * 根据code获取微信授权access_token
     * @param request
     */
    public static AuthToken getTokenByAuthCode(String code){
        AuthToken authToken;
        StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(WeChatConstant.GET_AUTHTOKEN_URL+"appid="+ WeChatConfig.APP_ID+"&secret="+ WeChatConfig.APP_SECRET+"&code="+code+"&grant_type=authorization_code");
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine ;
            while((inputLine=in.readLine())!=null){
                json.append(inputLine);
            }
            in.close();
            //将json字符串转成javaBean
            ObjectMapper om = new ObjectMapper();
            authToken =JSONObject.parseObject(json.toString(),AuthToken.class);
    } catch (Exception e) {
            throw new RuntimeException("微信工具类:根据授权code获取access_token异常",e);
        }
        return authToken;
    }
 
    /**
     * 获取微信签名
     * @param map 请求参数集合
     * @return 微信请求签名串
     */
    public static String getSign(SortedMap<String,Object> map){
        StringBuffer sb = new StringBuffer();
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //参数中sign、key不参与签名加密
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)){
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WeChatConfig.KEY);
        String sign = MD5Util.MD5Encode(sb.toString(),"UTF-8").toUpperCase();
        return sign;
    }
 
    /**
     * 解析微信服务器发来的请求
     * @param inputStream
     * @return 微信返回的参数集合
     */
    public static SortedMap<String,Object> parseXml(InputStream inputStream) {
        SortedMap<String,Object> map = new TreeMap<String,Object>();
        try {
            //获取request输入流
            SAXReader reader = new SAXReader();
            org.dom4j.Document document = reader.read(inputStream);
            //得到xml根元素
            Element root = document.getRootElement();
            //得到根元素所有节点
            List<Element> elementList = root.elements();
            //遍历所有子节点
            for (Element element:elementList){
                map.put(element.getName(),element.getText());
            }
            //释放资源
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("微信工具类:解析xml异常",e);
        }
        return map;
    }
 
    /**
     * 扩展xstream,使其支持name带有"_"的节点
     */
    public static XStream xStream = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_","_")));
 
    /**
     * 请求参数转换成xml
     * @param data
     * @return xml字符串
     */
    public static String sendDataToXml(WxPaySendData data){
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", WxPaySendData.class);
        return xStream.toXML(data);
    }
 
    /**
     *  获取当前时间戳
     * @return 当前时间戳字符串
     */
    public static String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }
 
    /**
     * 获取指定长度的随机字符串
     * @param length
     * @return 随机字符串
     */
    public static String getRandomStr(int length){
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
