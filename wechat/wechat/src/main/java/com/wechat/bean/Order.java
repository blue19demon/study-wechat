package com.wechat.bean;

import java.util.Date;

public class Order {
	 private String orderNumber;
	 private int sumFee;
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getSumFee() {
		return sumFee;
	}
	public void setSumFee(int sumFee) {
		this.sumFee = sumFee;
	}
	
	public Order(int sumFee) {
		super();
		this.orderNumber = String.valueOf(new Date().getTime());
		this.sumFee = sumFee;
	}
	@Override
	public String toString() {
		return "Order [orderNumber=" + orderNumber + ", sumFee=" + sumFee + "]";
	}
	
	
}
