package com.starter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starter.config.app.AppConfiguration;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@Service
@Slf4j
public class MenuInitService {
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;
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
			button023.setUrl(appConfiguration.getServerUrl()+"/oauth");
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
			button025.setUrl(appConfiguration.getServerUrl()+"/myLocation");
			subButtons02.add(button025);
			
			button02.setSubButtons(subButtons02);
			buttons.add(button02);
			
			WxMenuButton button03 = new WxMenuButton();
			button03.setName("更多");
			button03.setType("click");
			button03.setKey("BIG100");
			List<WxMenuButton> subButtons03 = new ArrayList<>();
			
			WxMenuButton button031 = new WxMenuButton();
			button031.setName("我的名片");
			button031.setType("click");
			button031.setKey("MY_CARD");
			subButtons03.add(button031);
			
			WxMenuButton button032 = new WxMenuButton();
			button032.setName("支付宝体验更多");
			button032.setType("click");
			button032.setKey("ALI_PAY");
			subButtons03.add(button032);
			
			WxMenuButton button033 = new WxMenuButton();
			button033.setName("远程控制服务器");
			button033.setType("click");
			button033.setKey("REMOTE_CONTROLLER");
			subButtons03.add(button033);
			
			WxMenuButton button034 = new WxMenuButton();
			button034.setName("主菜单");
			button034.setType("click");
			button034.setKey("MAIN_MENU");
			subButtons03.add(button034);
			
			button03.setSubButtons(subButtons03);
			buttons.add(button03);
			
			menu.setButtons(buttons);
			wxMpService.getMenuService().menuCreate(menu);
			
			log.info("serverUrl:"+appConfiguration.getServerUrl());
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}
}
