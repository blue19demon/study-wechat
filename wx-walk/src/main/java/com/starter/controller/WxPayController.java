package com.starter.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import com.starter.config.disconf.AppConfiguration;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WxPayController {

	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private AppConfiguration appConfiguration;

	@RequestMapping(value = "h5pay")
	public ModelAndView pay(HttpServletRequest request, Map<String, Object> map)  throws Exception {
		String orderNo = IdUtil.simpleUUID();
		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			orderRequest.setBody("测试");
			orderRequest.setOutTradeNo(orderNo);
			orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen("1.79"));// 元转成分
			orderRequest.setOpenid("oUXo10eAYReIil6dUcD5M0aAJPlI");
			orderRequest.setSpbillCreateIp(request.getRemoteAddr());
			Date now = new Date();
			orderRequest.setTimeStart(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMAT));
			orderRequest.setTimeExpire(DateUtil.format(DateUtil.tomorrow(), DatePattern.PURE_DATETIME_FORMAT));
			orderRequest.setNotifyUrl(appConfiguration.getServerUrl()+"/payNotify");
			orderRequest.setTradeType("JSAPI");
			orderRequest.setDeviceInfo("sandbox");//沙箱环境
			orderRequest.setSpbillCreateIp(request.getRemoteAddr());
			WxPayMpOrderResult wxPayAppOrderResult = wxPayService.createOrder(orderRequest);
			/**
			 * {
					"appId":"wx4492fde581248a1d",
					"nonceStr":"1571971670514",
					"packageValue":"prepay_id=wx20191025104752913740",
					"paySign":"9E00436DE48FC065F20C74EA170A3E72",
					"signType":"MD5",
					"timeStamp":"1571971670"
				}
			 */
			log.info("微信下单结果->"+ JSONObject.toJSONString(wxPayAppOrderResult, true));
			map.put("payResponse", wxPayAppOrderResult);
		} catch (Exception e) {
			log.error("微信支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
			e.printStackTrace();
		}
		return new ModelAndView("h5pay", map);
	}

	@ResponseBody
	@RequestMapping("/payNotify")
	public String payNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
			WxPayOrderNotifyResult  wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xmlResult);
			/**
			 * {
					"appid":"wx4492fde581248a1d",
					"attach":"sandbox_attach",
					"bankType":"CMC",
					"cashFee":179,
					"cashFeeType":"CNY",
					"couponList":[],
					"deviceInfo":"sandbox",
					"errCode":"SUCCESS",
					"errCodeDes":"SUCCESS",
					"feeType":"CNY",
					"isSubscribe":"Y",
					"mchId":"1512382871",
					"nonceStr":"1571971667651",
					"openid":"oUXo10eAYReIil6dUcD5M0aAJPlI",
					"outTradeNo":"8168403a219f4ea388ef5f4caea17f9f",
					"resultCode":"SUCCESS",
					"returnCode":"SUCCESS",
					"returnMsg":"OK",
					"settlementTotalFee":179,
					"sign":"1D6E0DB0958EADB6C49687359492B4A4",
					"timeEnd":"20191025104754",
					"totalFee":179,
					"tradeType":"JSAPI",
					"transactionId":"4320293721920191025104754582706",
					"xmlString":"<xml>\n  <openid><![CDATA[oUXo10eAYReIil6dUcD5M0aAJPlI]]></openid>\n  <trade_type><![CDATA[JSAPI]]></trade_type>\n  <cash_fee_type><![CDATA[CNY]]></cash_fee_type>\n  <nonce_str><![CDATA[1571971667651]]></nonce_str>\n  <time_end><![CDATA[20191025104754]]></time_end>\n  <err_code_des><![CDATA[SUCCESS]]></err_code_des>\n  <return_code><![CDATA[SUCCESS]]></return_code>\n  <mch_id><![CDATA[1512382871]]></mch_id>\n  <settlement_total_fee><![CDATA[179]]></settlement_total_fee>\n  <sign><![CDATA[1D6E0DB0958EADB6C49687359492B4A4]]></sign>\n  <cash_fee><![CDATA[179]]></cash_fee>\n  <is_subscribe><![CDATA[Y]]></is_subscribe>\n  <return_msg><![CDATA[OK]]></return_msg>\n  <fee_type><![CDATA[CNY]]></fee_type>\n  <bank_type><![CDATA[CMC]]></bank_type>\n  <attach><![CDATA[sandbox_attach]]></attach>\n  <device_info><![CDATA[sandbox]]></device_info>\n  <out_trade_no><![CDATA[8168403a219f4ea388ef5f4caea17f9f]]></out_trade_no>\n  <result_code><![CDATA[SUCCESS]]></result_code>\n  <total_fee><![CDATA[179]]></total_fee>\n  <appid><![CDATA[wx4492fde581248a1d]]></appid>\n  <transaction_id><![CDATA[4320293721920191025104754582706]]></transaction_id>\n  <err_code><![CDATA[SUCCESS]]></err_code>\n</xml>"
				}
			 */
			log.info("微信回调结果->"+ JSONObject.toJSONString(wxPayOrderNotifyResult, true));
			// 自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
			return WxPayNotifyResponse.success("处理成功!");
		} catch (Exception e) {
			log.error("微信回调结果异常,异常原因{}", e.getMessage());
			return WxPayNotifyResponse.fail(e.getMessage());
		}
	}
}
