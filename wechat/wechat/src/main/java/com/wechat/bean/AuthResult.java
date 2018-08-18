package com.wechat.bean;

import java.io.Serializable;

/**
 * 授权登录结果
 * @author Administrator
 *
 */
public class AuthResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String country;
	private String province;
	private String city;
	private String openid;
	private String sex;
	private String nickname;
	private String headimgurl;
	private String language;
	private String[] privilege;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getSex() {
		if("1".equals(sex)) {
			return "男";
		}
		if("2".equals(sex)) {
			return "女";
		}
		return "未知";
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getLanguage() {
		if("zh_CN".equals(language)) {
			return "中文";
		}
		return "外文";
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String[] getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}
	
	
}
