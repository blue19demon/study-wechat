package com.github.binarywang.demo.wx.pay.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.binarywang.demo.wx.pay.utils.GenerateQrCodeUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.github.binarywang.wxpay.service.WxPayService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Api("微信支付demo")
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
	private WxPayService wxService;

	@Autowired
	public DemoController(WxPayService wxService) {
		this.wxService = wxService;
	}

	@ResponseBody
	@RequestMapping(value = "/wxpay")
	public ModelAndView pay(HttpServletRequest request, Map<String, Object> map) {
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
			WxPayMpOrderResult wxPayAppOrderResult = wxService.createOrder(orderRequest);
			map.put("payResponse", wxPayAppOrderResult);
		} catch (Exception e) {
			log.error("微信支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
			e.printStackTrace();
		}
		return new ModelAndView("pay", map);
	}
	
	
	/**
	 * 扫码支付
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/qrCodePay")
	public ModelAndView qrCodePay(HttpServletRequest request, Map<String, Object> map) {
		String orderNo = IdUtil.simpleUUID();
		String productId = IdUtil.simpleUUID();
		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			orderRequest.setBody("扫码测试");
			orderRequest.setOutTradeNo(orderNo);
			orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen("1.79"));// 元转成分
			orderRequest.setOpenid("oUXo10eAYReIil6dUcD5M0aAJPlI");
			orderRequest.setSpbillCreateIp(request.getRemoteAddr());
			orderRequest.setTradeType(TradeType.NATIVE);
			orderRequest.setProductId(productId);
			Date now = new Date();
			orderRequest.setTimeStart(DateUtil.format(now, DatePattern.PURE_DATETIME_FORMAT));
			orderRequest.setTimeExpire(DateUtil.format(DateUtil.tomorrow(), DatePattern.PURE_DATETIME_FORMAT));
			WxPayNativeOrderResult wxPayNativeOrderResult = wxService.createOrder(orderRequest);
			log.info("wxPayNativeOrderResult->"+new JSONObject(wxPayNativeOrderResult).toStringPretty());
			map.put("code_url", wxPayNativeOrderResult.getCodeUrl());
		} catch (Exception e) {
			log.error("微信扫码支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
			e.printStackTrace();
		}
		return new ModelAndView("saomaPay", map);
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
    
	
	@RequestMapping("/success")
	public ModelAndView success(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("success");
	}
	
	@ResponseBody
	@RequestMapping("/payNotify")
	public String payNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
			WxPayOrderNotifyResult result = wxService.parseOrderNotifyResult(xmlResult);
			// 结果正确
			log.info("结果正确"+result.toString());
			// 自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
			return WxPayNotifyResponse.success("处理成功!");
		} catch (Exception e) {
			log.error("微信回调结果异常,异常原因{}", e.getMessage());
			return WxPayNotifyResponse.fail(e.getMessage());
		}
	}
	
}
