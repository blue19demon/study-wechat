package com.starter.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starter.service.MenuInitService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StepExecutor implements Runnable {
	@Autowired
	private MenuInitService menuInitService;

	@Override
	public void run() {
		startStreamTask();
	}

	public void startStreamTask() {
		log.info("menu 重置");
		menuInitService.menuCreate();
	}

}