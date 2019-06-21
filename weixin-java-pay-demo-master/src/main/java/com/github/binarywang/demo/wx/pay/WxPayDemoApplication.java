package com.github.binarywang.demo.wx.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Binary Wang
 */
@SpringBootApplication
public class WxPayDemoApplication {
	public static void main(String[] args) {
		System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","{}");
		SpringApplication.run(WxPayDemoApplication.class, args);
	}
}
