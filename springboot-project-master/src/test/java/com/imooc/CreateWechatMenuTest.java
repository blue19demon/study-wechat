package com.imooc;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * Created by SqMax on 2018/3/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CreateWechatMenuTest {

	@Autowired
	private WxMpService wxMpService;
    @Test
    public void create() {
    	try {
    		WxMenu menu=new WxMenu();
    		WxMenuButton button1=new WxMenuButton();
    		button1.setName("父菜单1");
    		button1.setType("click");
    		WxMenuButton sub1=new WxMenuButton();
    		sub1.setName("子菜单1-1");
    		sub1.setType("view");
    		sub1.setUrl("http://www.baidu.com");
    		WxMenuButton sub2=new WxMenuButton();
    		sub2.setName("子菜单1-2");
    		sub2.setType("view");
    		sub2.setUrl("http://www.sougou.com");
    		button1.setSubButtons(Arrays.asList(
    				sub1,
    				sub2
    				));
    		WxMenuButton button2=new WxMenuButton();
    		button2.setName("父菜单2");
    		button2.setType("click");
    		WxMenuButton sub3=new WxMenuButton();
    		sub3.setName("子菜单2-1");
    		sub3.setType("view");
    		sub3.setUrl("http://www.baidu.com");
    		WxMenuButton sub4=new WxMenuButton();
    		sub4.setName("子菜单2-2");
    		sub4.setType("view");
    		sub4.setUrl("http://www.sougou.com");
    		WxMenuButton sub5=new WxMenuButton();
    		sub5.setName("子菜单2-3");
    		sub5.setType("view");
    		sub5.setUrl("http://www.sougou.com");
    		button2.setSubButtons(Arrays.asList(
    				sub3,
    				sub4,
    				sub5
    				));
    		menu.setButtons(Arrays.asList(
    				button1,
    				button2
    				));
			log.info("【创建微信菜单】 result={}",wxMpService.getMenuService().menuCreate(menu));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
        
    }

}