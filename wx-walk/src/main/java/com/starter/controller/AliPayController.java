package com.starter.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.starter.config.app.AliAPIConfiguration;
import com.starter.config.app.AppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AliPayController {

	@Autowired
	private AlipayClient client;// 支付宝请求sdk客户端

	@Autowired
	private AliAPIConfiguration aliAPIConfiguration;// 支付宝sdk配置

	@Autowired
	private AppConfiguration appConfiguration;

	@RequestMapping(value = "/aliH5pay")
	public void aliH5pay(HttpServletResponse httpResponse) throws Exception {
		String out_trade_no = String.valueOf(new Date().getTime());
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
		model.setSubject("测试订单");
		model.setTotalAmount("0.01");
		model.setBody("测试订单");
		model.setTimeoutExpress("2m");
		model.setProductCode("QUICK_WAP_PAY");
		// 移动H5支付
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		request.setNotifyUrl(appConfiguration.getServerUrl() + "/aliPayNotify"); // 异步通知地址
		request.setReturnUrl(appConfiguration.getServerUrl() + "/aliPayReturn");
		request.setBizModel(model); // 业务参数
		// 移动H5支付
		try {
			PrintWriter writer = httpResponse.getWriter();
			httpResponse.setContentType("text/html;charset=UTF-8");
			writer.write(client.pageExecute(request).getBody());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "aliPayReturn")
	public ModelAndView aliPayReturn(HttpServletRequest request, Model model) throws Exception {
		try {
			//获取支付宝GET过来反馈信息
	        Map<String,String> params = new HashMap<String,String>();
	        Map<String,String[]> requestParams = request.getParameterMap();
	        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
	            String name = (String) iter.next();
	            String[] values = (String[]) requestParams.get(name);
	            String valueStr = "";
	            for (int i = 0; i < values.length; i++) {
	                valueStr = (i == values.length - 1) ? valueStr + values[i]
	                        : valueStr + values[i] + ",";
	            }
	            //乱码解决，这段代码在出现乱码时使用
	            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
	            params.put(name, valueStr);
	        }
	        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliAPIConfiguration.getAlipayPublicKey(),AlipayConstants.CHARSET_UTF8, aliAPIConfiguration.getSignType()); //调用SDK验证签名
	        //——请在这里编写您的程序（以下代码仅作参考）——
	        if(signVerified) {
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

				model.addAttribute("trade_no", trade_no);
				model.addAttribute("out_trade_no", out_trade_no);
				model.addAttribute("total_amount", total_amount);
			} else {
				System.out.println("验签失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("aliPayReturn");
	}

	@ResponseBody
	@PostMapping("/aliPayNotify")
	public String aliPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params,  aliAPIConfiguration.getAlipayPublicKey(),AlipayConstants.CHARSET_UTF8, aliAPIConfiguration.getSignType()); //调用SDK验证签名
        //——请在这里编写您的程序（以下代码仅作参考）——
        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        */
        if(!signVerified) {//验证成功
			// 这里处理验签失败
			log.error("验签失败");
		}
      //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        if(trade_status.equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        }else if (trade_status.equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //付款完成后，支付宝系统发送该交易状态通知

            log.info("********************** 支付成功(支付宝同步通知) **********************");
            log.info("支付宝回调结果->" + JSONObject.toJSONString(requestParams, true));
            log.info("***************************************************************");
        }
		
		return "success";
	}
}
