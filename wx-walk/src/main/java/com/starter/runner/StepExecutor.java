package com.starter.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starter.service.MenuInitService;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;

@Component
@Slf4j
public class StepExecutor implements Runnable {
	@Autowired
	private MenuInitService menuInitService;
	@Autowired
	private WxMpService wxMpService;
	@Override
	public void run() {
		startStreamTask();
	}

	public void startStreamTask() {
		log.info("menu 重置");
		menuInitService.menuCreate();
		//addKefu();
		//log.info("添加客服");
	}

	public void addKefu() {
		WxMpKfAccountRequest request=new WxMpKfAccountRequest("o0BJQ5uR7L6QjrwRek8sklsuOXpc", "blue deoman", null);
		try {
			log.info(""+wxMpService.getKefuService().kfAccountAdd(request));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

}