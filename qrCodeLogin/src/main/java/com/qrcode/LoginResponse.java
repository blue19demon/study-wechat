package com.qrcode;
import java.util.concurrent.CountDownLatch;

/**
 * 登录信息承载类
 *
 */
public class LoginResponse {

    public CountDownLatch latch;

    public String user;

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

    
}