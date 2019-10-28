package com.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.starter.runner.ApplicationStartup;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.starter.mapper")
public class WxWalkApplication {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(WxWalkApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.run(args);
	}

}
