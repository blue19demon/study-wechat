package com.github.binarywang.demo.wx.pay.test;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.binarywang.wxpay.bean.request.WxPaySendRedpackRequest;
import com.github.binarywang.wxpay.bean.result.WxPaySendRedpackResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayDemoApplicationTests {
	@Autowired
	private WxPayService wxService;

	@Test
	public void contextLoads() {
		WxPaySendRedpackRequest request = new WxPaySendRedpackRequest();
		try {
			Date now = new Date();
			request.setMchBillNo("mch_id".concat(DateUtil.format(now, "yyyyMMdd")).concat(getRandomID()));
			request.setSendName("商户名称");
			request.setReOpenid("oUXo10eAYReIil6dUcD5M0aAJPlI");
			request.setTotalAmount(1);
			request.setTotalNum(1);
			WxPaySendRedpackResult result = this.wxService.sendRedpack(request);
			log.info("result=" + new JSONObject(result).toStringPretty());
		} catch (WxPayException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private static String getRandomID() {
		Random r = new Random();
		StringBuffer sb = new StringBuffer(10);

		for (int j = 1; j <= 10; j++) {

			int i = r.nextInt(10);

			if (j == 1 || (j >= 8 && j <= 10)) {
				while (i == 0) {
					i = r.nextInt(10);
				}
			}

			sb.append(i);
		}
		System.out.println("random num is:" + sb.toString());
		return sb.toString();
	}
}
