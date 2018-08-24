package com.wechat.servcie;

import org.springframework.stereotype.Service;

import com.wechat.bean.Order;

@Service
public class OrderService {

	public Order get(String state) {
		return new Order(100);
	}

}
