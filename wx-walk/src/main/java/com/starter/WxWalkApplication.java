package com.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.starter.mapper")
public class WxWalkApplication {
	public static void main(String[] args) {
		SpringApplication.run(WxWalkApplication.class, args);
	}

}
