package com.remote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RemoteControllerApplication {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(RemoteControllerApplication.class);
		springApplication.run(args);
	}

}
