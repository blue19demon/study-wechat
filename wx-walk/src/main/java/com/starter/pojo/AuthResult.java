package com.starter.pojo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String account;
	private String nickname;
	/**
	 * 性别描述信息：男、女、未知等.
	 */
	private String sexDesc;
	/**
	 * 性别表示：1，2等数字.
	 */
	private Integer sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private String headImgUrl;
}
