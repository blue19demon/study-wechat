package com.wechat.controller;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.wechat.bean.Order;
import com.wechat.bean.WxPaySendData;
import com.wechat.config.WeChatConfig;
import com.wechat.utils.StringUtils;
import com.wechat.utils.WeChatUtils;

public class WeChatPayService {
	/**
	*微信支付统一下单
	**/
	public static Map<String,String> unifiedOrder(Order order, Map<String,Object> map,String type){
	    Map<String, String> resultMap = null;
	    try {
	            WxPaySendData paySendData = new WxPaySendData();
	            //构建微信支付请求参数集合
	            paySendData.setAppId(WeChatConfig.APP_ID);
	            paySendData.setAttach("微信订单支付:"+order.getOrderNumber());
	            paySendData.setBody("商品描述");
	            paySendData.setMchId(WeChatConfig.MCH_ID);
	            paySendData.setNonceStr(WeChatUtils.getRandomStr(32));
	            paySendData.setNotifyUrl(WeChatConfig.NOTIFY_URL);
	            paySendData.setDeviceInfo("WEB");
	            paySendData.setOutTradeNo(order.getOrderNumber());
	            paySendData.setTotalFee(179);
	           if("1".equals(type)) {
	        	   paySendData.setTradeType(WeChatConfig.TRADE_TYPE_JSAPI);
	           }else {
	        	   paySendData.setTradeType(WeChatConfig.TRADE_TYPE_NATIVE);
	           }
	            paySendData.setSpBillCreateIp((String) map.get("remoteIp"));
	            paySendData.setOpenId((String) map.get("openId"));
	            //将参数拼成map,生产签名
	            SortedMap<String,Object> params = buildParamMap(paySendData);
	            paySendData.setSign(WeChatUtils.getSign(params));
	            //将请求参数对象转换成xml
	            String reqXml = WeChatUtils.sendDataToXml(paySendData);
	            //发送请求
	            byte[] xmlData = reqXml.getBytes();
	            URL url = new URL(WeChatConfig.UNIFIED_ORDER_URL);
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.setDoOutput(true);
	            urlConnection.setDoInput(true);
	            urlConnection.setUseCaches(false);
	            urlConnection.setRequestProperty("Content_Type","text/xml");
	            urlConnection.setRequestProperty("Content-length",String.valueOf(xmlData.length));
	            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
	            outputStream.write(xmlData);
	            outputStream.flush();
	            outputStream.close();
	            resultMap = WeChatUtils.parseXml(urlConnection.getInputStream());
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return resultMap;
	}
	/**
     * 构建统一下单参数map 用于生成签名
     * @param data
     * @return SortedMap<String,Object>
     */
	private static SortedMap<String, Object> buildParamMap(WxPaySendData data) {
		SortedMap<String,Object> paramters = new TreeMap<String, Object>();
        if (null != data){
            if (StringUtils.isNotEmpty(data.getAppId())){
                paramters.put("appid",data.getAppId());
            }
            if (StringUtils.isNotEmpty(data.getAttach())){
                paramters.put("attach",data.getAttach());
            }
            if (StringUtils.isNotEmpty(data.getBody())){
                paramters.put("body",data.getBody());
            }
            if (StringUtils.isNotEmpty(data.getDetail())){
                paramters.put("detail",data.getDetail());
            }
            if (StringUtils.isNotEmpty(data.getDeviceInfo())){
                paramters.put("device_info",data.getDeviceInfo());
            }
            if (StringUtils.isNotEmpty(data.getFeeType())){
                paramters.put("fee_type",data.getFeeType());
            }
            if (StringUtils.isNotEmpty(data.getGoodsTag())){
                paramters.put("goods_tag",data.getGoodsTag());
            }
            if (StringUtils.isNotEmpty(data.getLimitPay())){
                paramters.put("limit_pay",data.getLimitPay());
            }
            if (StringUtils.isNotEmpty(data.getMchId())){
                paramters.put("mch_id",data.getMchId());
            }
            if (StringUtils.isNotEmpty(data.getNonceStr())){
                paramters.put("nonce_str",data.getNonceStr());
            }
            if (StringUtils.isNotEmpty(data.getNotifyUrl())){
                paramters.put("notify_url",data.getNotifyUrl());
            }
            if (StringUtils.isNotEmpty(data.getOpenId())){
                paramters.put("openid",data.getOpenId());
            }
            if (StringUtils.isNotEmpty(data.getOutTradeNo())){
                paramters.put("out_trade_no",data.getOutTradeNo());
            }
            if (StringUtils.isNotEmpty(data.getSign())){
                paramters.put("sign",data.getSign());
            }
            if (StringUtils.isNotEmpty(data.getSpBillCreateIp())){
                paramters.put("spbill_create_ip",data.getSpBillCreateIp());
            }
            if (StringUtils.isNotEmpty(data.getTimeStart())){
                paramters.put("time_start",data.getTimeStart());
            }
            if (StringUtils.isNotEmpty(data.getTimeExpire())){
                paramters.put("time_expire",data.getTimeExpire());
            }
            if (StringUtils.isNotEmpty(data.getProductId())){
                paramters.put("product_id",data.getProductId());
            }
            if (data.getTotalFee()>0){
                paramters.put("total_fee",data.getTotalFee());
            }
            if (StringUtils.isNotEmpty(data.getTradeType())){
                paramters.put("trade_type",data.getTradeType());
            }
            //申请退款参数
            if (StringUtils.isNotEmpty(data.getTransactionId())){
                paramters.put("transaction_id",data.getTransactionId());
            }
            if (StringUtils.isNotEmpty(data.getOutRefundNo())){
                paramters.put("out_refund_no",data.getOutRefundNo());
            }
            if (StringUtils.isNotEmpty(data.getOpUserId())){
                paramters.put("op_user_id",data.getOpUserId());
            }
            if (StringUtils.isNotEmpty(data.getRefundFeeType())){
                paramters.put("refund_fee_type",data.getRefundFeeType());
            }
            if (null != data.getRefundFee() && data.getRefundFee()>0){
                paramters.put("refund_fee",data.getRefundFee());
            }
            if (StringUtils.isNotEmpty(data.getCodeUrl())){
                paramters.put("code_url",data.getCodeUrl());
            }
        }
        return paramters;
	}
}
