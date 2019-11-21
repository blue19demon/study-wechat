package com.starter.pojo;

/**
 * 地址信息
 */
public class GaodePlace implements Comparable<GaodePlace> {
	// 名称
	private String name;
	// 详细地址
	private String address;
	// 经度，纬度
	private String location;
	// 联系电话
	private String tel;
	// 距离
	private int distance;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public GaodePlace() {
		super();
	}


	public GaodePlace(String name, String address, String location, String tel, int distance) {
		super();
		this.name = name;
		this.address = address;
		this.location = location;
		this.tel = tel;
		this.distance = distance;
	}


	@Override
	public int compareTo(GaodePlace baiduPlace) {
		return this.distance - baiduPlace.getDistance();
	}
}
