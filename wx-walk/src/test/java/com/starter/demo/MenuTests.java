package com.starter.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.starter.config.AppConfiguration;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MenuTests {
	
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;
	@Test
	public void menuDelete() {
		try {
			wxMpService.getMenuService().menuDelete();
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getSelfMenuInfo() {
		try {
			log.info(JSONObject.toJSONString(wxMpService.getMenuService().getSelfMenuInfo(), true));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void menuGet() {
		try {
			log.info(JSONObject.toJSONString(wxMpService.getMenuService().menuGet(), true));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void menuCreate() {
		try {
			WxMenu menu = new WxMenu();
			List<WxMenuButton> buttons = new ArrayList<WxMenuButton>();
			WxMenuButton button01 = new WxMenuButton();
			button01.setName("娱乐");
			button01.setType("click");
			button01.setKey("BIG01");
			buttons.add(button01);
			List<WxMenuButton> subButtons01 = new ArrayList<>();
			WxMenuButton button011 = new WxMenuButton();
			button011.setName("百度");
			button011.setType("view");
			button011.setKey("ITEM01");
			button011.setUrl("http://www.baidu.com");
			subButtons01.add(button011);

			WxMenuButton button021 = new WxMenuButton();
			button021.setName("搜狗");
			button021.setType("view");
			button021.setKey("ITEM03");
			button021.setUrl("http://www.sougou.com");
			subButtons01.add(button021);
			
			button01.setSubButtons(subButtons01);

			WxMenuButton button02 = new WxMenuButton();
			button02.setName("休闲");
			button02.setType("click");
			button02.setKey("BIG02");

			List<WxMenuButton> subButtons02 = new ArrayList<>();
			
			WxMenuButton button022 = new WxMenuButton();
			button022.setName("分享");
			button022.setType("view");
			button022.setKey("ITEM04");
			button022.setUrl(appConfiguration.getServerUrl()+"/index");
			subButtons02.add(button022);
			
			WxMenuButton button023 = new WxMenuButton();
			button023.setName("授权");
			button023.setType("view");
			button023.setKey("ITEM05");
			button023.setUrl(appConfiguration.getServerUrl()+"/authorize");
			subButtons02.add(button023);
			
			WxMenuButton button024 = new WxMenuButton();
			button024.setName("付款");
			button024.setType("view");
			button024.setKey("ITEM05");
			button024.setUrl(appConfiguration.getServerUrl()+"/h5pay");
			subButtons02.add(button024);
			
			WxMenuButton button025 = new WxMenuButton();
			button025.setName("我的位置");
			button025.setType("view");
			button025.setKey("ITEM06");
			button025.setUrl(appConfiguration.getServerUrl()+"/getLocation");
			subButtons02.add(button025);
			
			button02.setSubButtons(subButtons02);
			
			WxMenuButton button03 = new WxMenuButton();
			button03.setName("我的名片");
			button03.setType("click");
			button03.setKey("MY_CARD");
			buttons.add(button03);
			
			buttons.add(button02);
			menu.setButtons(buttons);
			wxMpService.getMenuService().menuCreate(menu);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

}
