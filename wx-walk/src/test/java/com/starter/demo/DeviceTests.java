package com.starter.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDeviceService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.device.WxDeviceBind;
import me.chanjar.weixin.mp.bean.device.WxDeviceBindDeviceResult;
import me.chanjar.weixin.mp.bean.device.WxDeviceBindResult;
import me.chanjar.weixin.mp.enums.TicketType;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DeviceTests {
	@Autowired
	private WxMpService wxMpService;

	@Test
	public void bindDevice() {
		try {
			WxMpDeviceService wxMpDeviceService = wxMpService.getDeviceService();
			WxDeviceBind wxDeviceBind=new WxDeviceBind();
			wxDeviceBind.setOpenId("o0BJQ5uR7L6QjrwRek8sklsuOXpc");
			wxDeviceBind.setDeviceId("iphone");
			String ticket = wxMpService.getTicket(TicketType.SDK, true);
			wxDeviceBind.setTicket(ticket);
			WxDeviceBindResult WxDeviceBindResult = wxMpDeviceService.bind(wxDeviceBind);
			log.info(JSONObject.toJSONString(WxDeviceBindResult, true));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getBindDevice() {
		try {
			WxMpDeviceService wxMpDeviceService = wxMpService.getDeviceService();
			WxDeviceBindDeviceResult device = wxMpDeviceService.getBindDevice("o0BJQ5uR7L6QjrwRek8sklsuOXpc");
			log.info(JSONObject.toJSONString(device, true));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
}
