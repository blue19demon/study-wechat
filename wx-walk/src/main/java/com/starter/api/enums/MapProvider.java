package com.starter.api.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum MapProvider {
	BAIDU("百度地图"),
	GAODE("高德地图");
	private String desc;

	public String getDesc() {
		return desc;
	}
	
	public static MapProvider getByName(String name) {
		for (MapProvider mapProvider : MapProvider.values()) {
			if(name.equals(mapProvider.name())) {
				return mapProvider;
			}
		}
		return null;
	}
}
