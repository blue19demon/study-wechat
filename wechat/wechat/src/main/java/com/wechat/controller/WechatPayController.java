package com.wechat.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.Order;
import com.wechat.config.PayConfig;
import com.wechat.config.WechatConstant;
import com.wechat.servcie.OrderService;
import com.wechat.servcie.WeChatPayService;
import com.wechat.utils.GenerateQrCodeUtil;
import com.wechat.utils.WeChatUtils;

/**
 * 微信支付
 * @author Administrator
 *
 */
@Controller("weChatOrderController")
public class WechatPayController{
	private final static Logger logger = LoggerFactory.getLogger(WechatPayController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "weChatPayTest")
    public String weChatPayTest(HttpServletRequest request,Model model){
		return "weChatPayTest";
    }
    
    @RequestMapping(value = "unifiedOrder")
    public String unifiedOrder(HttpServletRequest request,Model model) throws Exception{
        //用户同意授权，获得的code
        String code = request.getParameter("code");
        String type = request.getParameter("type");
        //请求授权携带的参数【根据自己需要设定值，此处我传的是订单id】
        String state = request.getParameter("state");
        Order order = orderService.get(state);//订单信息
        //通过code获取网页授权access_token
        //AuthToken authToken = WeChatUtils.getTokenByAuthCode(code);
        //构建微信统一下单需要的参数
		//logger.info("====authToken===="+JSONObject.toJSONString(authToken));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("openId","oUXo10eAYReIil6dUcD5M0aAJPlI");//用户标识openId
        map.put("remoteIp",request.getRemoteAddr());//请求Ip地址
        //调用统一下单service
        Map<String,String> resultMap = WeChatPayService.unifiedOrder(order,map,type);
        String returnCode = (String) resultMap.get("return_code");//通信标识
        String resultCode = (String) resultMap.get("result_code");//交易标识
        //只有当returnCode与resultCode均返回“success”，才代表微信支付统一下单成功
        if (WechatConstant.RETURN_SUCCESS.equals(resultCode)&&WechatConstant.RETURN_SUCCESS.equals(returnCode)){
            logger.info("微信统一下单成功:"+JSONObject.toJSONString(resultMap));
        	if("1".equals(type)) {
            	String appId = (String) resultMap.get("appid");//微信公众号AppId
                String timeStamp = WeChatUtils.getTimeStamp();//当前时间戳
                String prepayId = "prepay_id="+resultMap.get("prepay_id");//统一下单返回的预支付id
                String nonceStr = WeChatUtils.getRandomStr(20);//不长于32位的随机字符串
                SortedMap<String,Object> signMap = new TreeMap<String,Object>();//自然升序map
                signMap.put("appId",appId);
                signMap.put("package",prepayId);
                signMap.put("timeStamp",timeStamp);
                signMap.put("nonceStr",nonceStr);
                signMap.put("signType","MD5");
                model.addAttribute("appId",appId);
                model.addAttribute("timeStamp",timeStamp);
                model.addAttribute("nonceStr",nonceStr);
                model.addAttribute("prepayId",prepayId);
                model.addAttribute("paySign",WeChatUtils.getSign(signMap));//获取签名
                //将支付需要参数返回至页面，采用h5方式调用支付接口
                return "h5Pay";
            }else {
            	String appId = (String) resultMap.get("appid");//微信公众号AppId
                String timeStamp = WeChatUtils.getTimeStamp();//当前时间戳
                String prepayId = resultMap.get("prepay_id");//统一下单返回的预支付id
                String code_url = resultMap.get("code_url");//统一下单返回的预支付id
                String nonceStr = WeChatUtils.getRandomStr(20);//不长于32位的随机字符串
                SortedMap<String,Object> signMap = new TreeMap<String,Object>();//自然升序map
                signMap.put("appId",appId);
                signMap.put("package",prepayId);
                signMap.put("timeStamp",timeStamp);
                signMap.put("nonceStr",nonceStr);
                signMap.put("signType","MD5");
                model.addAttribute("code_url",code_url);
            	return "saomaPay";
            }
        }else {
            logger.error("微信统一下单失败,订单编号:"+order.getOrderNumber()+",失败原因:"+JSONObject.toJSONString(resultMap));
            return "redirect:/weChatPayTest";//支付下单失败，重定向至订单列表
        }
    }
    
    /**
     * 生成二维码图片并直接以流的形式输出到页面
     * @param code_url
     * @param response
     */
    @RequestMapping("qr_code.img")
    @ResponseBody
    public void getQRCode(String code_url,HttpServletResponse response){
      GenerateQrCodeUtil.encodeQrcode(code_url, response);
    }
    
    @RequestMapping(value = {
    		"getWeChatPayReturn",
    		"getWeChatRefundReturn"
    })
    public String getWeChatPayReturn(HttpServletRequest request) {
		try {
			InputStream inStream = request.getInputStream();
			if (inStream != null) {
				Map<String, String> resultMap = WeChatUtils.parseXml(inStream);
				if(WeChatUtils.isTenpaySign(resultMap, PayConfig.KEY)) {
					logger.info("验证签名通过！");
					logger.info("====resultMap===="+JSONObject.toJSONString(resultMap));
					if (resultMap.get("result_code").equals("SUCCESS")) {
						return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
					}
				}else {
					logger.info("验证签名失败！");
				}
			}
			// 通知微信支付系统接收到信息
			return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// 如果失败返回错误，微信会再次发送支付信息
		return "fail";
	}
}
